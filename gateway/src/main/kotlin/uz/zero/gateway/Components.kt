package uz.zero.gateway

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationConverter(
    private val authService: AuthService,
) : Converter<Jwt, Mono<JwtAuthenticationToken>> {
    override fun convert(source: Jwt): Mono<JwtAuthenticationToken> {
        return authService.getUserInfo(source.tokenValue)
            .flatMap { userInfo: Map<String, Any?> ->
                // Extract username with fallback to phoneNum if username is not present
                val username = (userInfo[USER_USERNAME_KEY] as? String) 
                    ?: (userInfo["phoneNum"] as? String) 
                    ?: throw BadCredentialsException("Missing username or phoneNum in user info response")
                
                // Extract role safely
                val role = (userInfo[USER_ROLE_KEY] as? String)
                    ?: throw BadCredentialsException("Missing role in user info response")
                
                val authorities = listOf(SimpleGrantedAuthority(role))
                val jwtToken = JwtAuthenticationToken(source, authorities, username)

                jwtToken.details = userInfo

                Mono.just(jwtToken)
            }.onErrorResume { ex ->
                val msg = "Failed to authenticate with token"
                val cause = if (ex is WebClientResponseException)
                    RuntimeException("${msg}: ${ex.statusCode}", ex)
                else ex
                Mono.error(BadCredentialsException(msg, cause))
            }
    }
}
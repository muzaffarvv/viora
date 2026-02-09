package uz.zero.auth.configs

import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenEndpointConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.token.*
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AccessTokenResponseAuthenticationSuccessHandler
import org.springframework.security.web.SecurityFilterChain
import uz.zero.auth.constants.JWT_ROLE_KEY
import uz.zero.auth.constants.JWT_USER_ID_KEY
import uz.zero.auth.model.security.CustomUserDetails
import uz.zero.auth.components.AuthServerAuthenticationConverter
import uz.zero.auth.components.AuthServerAuthenticationProvider
import uz.zero.auth.components.JwtAuthenticationConverter
import uz.zero.auth.constants.JWT_ORGANIZATION_ID_KEY
import uz.zero.auth.feign.OrganizationClient

@Configuration
class AuthorizationServerConfig {

    @Bean
    fun authorizationServerSettings(authorizationServerProperties: OAuth2AuthorizationServerProperties): AuthorizationServerSettings {
        return AuthorizationServerSettings.builder().issuer(authorizationServerProperties.issuer).build()
    }

    @Bean
    @Order(1)
    fun authorizationServerSecurityFilterChain(
        http: HttpSecurity,
        passwordGrantAuthenticationConverter: AuthServerAuthenticationConverter,
        passwordGrantAuthenticationProvider: AuthServerAuthenticationProvider,
        tokenHandler: OAuth2AccessTokenResponseAuthenticationSuccessHandler
    ): SecurityFilterChain {
        val authorizationServerConfigurer =
            OAuth2AuthorizationServerConfigurer.authorizationServer()

        http
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .securityMatcher(authorizationServerConfigurer.endpointsMatcher)
            .authorizeHttpRequests {
                it
                    .requestMatchers("/error")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .with(authorizationServerConfigurer) { authorizationServer: OAuth2AuthorizationServerConfigurer ->
                authorizationServer
                    .tokenEndpoint { tokenEndpoint: OAuth2TokenEndpointConfigurer ->
                        tokenEndpoint
                            .accessTokenRequestConverter(passwordGrantAuthenticationConverter)
                            .accessTokenResponseHandler(tokenHandler)
                            .authenticationProvider(passwordGrantAuthenticationProvider)
                    }
            }
        return http.build()
    }

    @Bean
    @Order(2)
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationConverter: JwtAuthenticationConverter
    ): SecurityFilterChain {
        return http
            .authorizeHttpRequests {
                it
                    .requestMatchers("/register", "/login").permitAll()
                    .requestMatchers("/user/**").permitAll()
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers("/internal/**").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { resourceServer ->
                resourceServer.jwt {
                    it.jwtAuthenticationConverter(jwtAuthenticationConverter)
                }
            }
            .csrf { csrf -> csrf.disable() }
            .build()
    }

    //Spring Authorization Serverda yaratilayotgan token
    // (JWT) ning ichiga qo'shimcha ma'lumotlarni qo'shib yuborish uchun ishlatiladigan interfeys.
    @Bean
    fun jwtCustomizer(organizationClient: OrganizationClient): OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer { context ->
            if (context.tokenType == OAuth2TokenType.ACCESS_TOKEN) {

                val authentication = context.getPrincipal<Authentication>()
                val principal = authentication.principal

                if (principal is CustomUserDetails) {

                    val userId = principal.getUserId()

                    val explicitOrgId = context.authorization?.attributes?.get(JWT_ORGANIZATION_ID_KEY) as? Long

                    val orgIdToUse = explicitOrgId ?: try {
                        // FALLBACK: If no explicit org requested, use the user's active organization.
                        organizationClient.getActiveOrganization(userId).organizationId
                    } catch (e: Exception) {
                        null
                    }

                    context.claims.claim(JWT_USER_ID_KEY, userId)
                    context.claims.claim(JWT_ROLE_KEY, principal.authorities.map { it.authority })
                    
                    if (orgIdToUse != null) {
                        context.claims.claim(JWT_ORGANIZATION_ID_KEY, orgIdToUse)
                    }
                }
            }
        }
    }


    @Bean
    fun delegatingOAuth2TokenGenerator(
        jwtEncoder: JwtEncoder,
        jwtCustomizer: OAuth2TokenCustomizer<JwtEncodingContext>  // <-- Bean sifatida olinadi
    ): DelegatingOAuth2TokenGenerator {

        val jwtGenerator = JwtGenerator(jwtEncoder)
        jwtGenerator.setJwtCustomizer(jwtCustomizer)   // <-- bu yerda bean to‘g‘ri keladi

        return DelegatingOAuth2TokenGenerator(
            jwtGenerator,
            OAuth2RefreshTokenGenerator()
        )
    }


}
package uz.zero.auth.components

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.*
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator
import org.springframework.stereotype.Component
import uz.zero.auth.model.security.AuthServerAuthenticationToken
import uz.zero.auth.services.security.AuthServerProvider
import uz.zero.auth.services.security.JpaOAuth2AuthorizationService
import java.security.Principal

@Component
class AuthServerAuthenticationProvider(
    private val authServerProviders: List<AuthServerProvider>,
    private val tokenGenerator: OAuth2TokenGenerator<out OAuth2Token>,
    private val authorizationService: JpaOAuth2AuthorizationService
) : AuthenticationProvider {

    private val providerMap = authServerProviders.associateBy { it.grantType() }

    override fun authenticate(authentication: Authentication): Authentication {
        val authenticationToken = authentication as AuthServerAuthenticationToken
        val grantType = authenticationToken.grantTypes

        val clientPrincipal = authentication.getClientPrincipal()
        val registeredClient = clientPrincipal.registeredClient
            ?: throw OAuth2AuthenticationException("Invalid client")

        var authorization = providerMap[grantType]
            ?.provide(registeredClient, authenticationToken)
            ?: throw OAuth2AuthenticationException("${grantType.key} provider not found")

        // INJECTION: If Organization ID is present, we must inject it into the Authorization attributes
        // BEFORE the token context is built. This ensures the token customizer can access it.
        if (authenticationToken.organizationId != null) {
            authorization = OAuth2Authorization.from(authorization)
                .attribute(uz.zero.auth.constants.JWT_ORGANIZATION_ID_KEY, authenticationToken.organizationId)
                .build()
        }

        val principal = authorization.getAttribute<Authentication>(Principal::class.java.name)

        @Suppress("DEPRECATION")
        val tokenContextBuilder = DefaultOAuth2TokenContext.builder()
            .registeredClient(registeredClient)
            .principal(principal)
            .authorizationServerContext(AuthorizationServerContextHolder.getContext())
            .authorization(authorization)
            .authorizedScopes(authorization.authorizedScopes)
            .authorizationGrantType(AuthorizationGrantType.PASSWORD)
            .authorizationGrant(authentication)

        val authorizationBuilder = OAuth2Authorization.from(authorization)

        // ===== ACCESS TOKEN =====
        val accessTokenContext = tokenContextBuilder
            .tokenType(OAuth2TokenType.ACCESS_TOKEN)
            .build()

        val generatedAccessToken = tokenGenerator.generate(accessTokenContext)
            ?: throw OAuth2AuthenticationException("Failed to generate access token")

        val accessToken = buildAccessToken(
            authorizationBuilder,
            generatedAccessToken,
            accessTokenContext
        )

        // ===== REFRESH TOKEN =====
        var refreshToken: OAuth2RefreshToken? = null
        if (registeredClient.authorizationGrantTypes.contains(AuthorizationGrantType.REFRESH_TOKEN)) {
            val refreshTokenContext = tokenContextBuilder
                .tokenType(OAuth2TokenType.REFRESH_TOKEN)
                .build()

            refreshToken = tokenGenerator.generate(refreshTokenContext) as? OAuth2RefreshToken
                ?: throw OAuth2AuthenticationException("Failed to generate refresh token")

            authorizationBuilder.refreshToken(refreshToken)
        }

        authorization = authorizationBuilder.build()
        authorizationService.save(authorization)

        return OAuth2AccessTokenAuthenticationToken(
            registeredClient,
            clientPrincipal,
            accessToken,
            refreshToken
        )
    }

    override fun supports(authentication: Class<*>): Boolean =
        AuthServerAuthenticationToken::class.java.isAssignableFrom(authentication)

    private fun buildAccessToken(
        builder: OAuth2Authorization.Builder,
        token: OAuth2Token,
        context: OAuth2TokenContext
    ): OAuth2AccessToken {

        val accessToken = OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            token.tokenValue,
            token.issuedAt,
            token.expiresAt,
            context.authorizedScopes
        )

        val format = context.registeredClient.tokenSettings.accessTokenFormat

        builder.token(accessToken) { metadata ->
            if (token is ClaimAccessor) {
                metadata[OAuth2Authorization.Token.CLAIMS_METADATA_NAME] = token.claims
            }
            metadata[OAuth2Authorization.Token.INVALIDATED_METADATA_NAME] = false
            metadata[OAuth2TokenFormat::class.java.name] = format.value
            // metadata[]
        }

        return accessToken
    }
}

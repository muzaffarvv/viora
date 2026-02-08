package uz.zero.auth.components

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.stereotype.Component
import uz.zero.auth.enums.AuthServerGrantType
import uz.zero.auth.model.security.AuthServerAuthenticationToken
import uz.zero.auth.utils.getClientPrincipal


@Component
class AuthServerAuthenticationConverter : AuthenticationConverter {
    override fun convert(request: HttpServletRequest): Authentication? {
        val grantTypeParameter = request.getParameter("grant_type") ?: return null
        val grantType = AuthServerGrantType.findByKey(grantTypeParameter) ?: return null
        val clientPrincipal: OAuth2ClientAuthenticationToken = getClientPrincipal() ?: return null
        // EXTRACTION: We now support explicit organization context via 'organization_id' parameter.
        // This allows users to request tokens for a specific organization (switching context).
        val organizationId = request.getParameter("organization_id")?.toLongOrNull()
        return AuthServerAuthenticationToken(request.parameterMap, grantType, organizationId, clientPrincipal)
    }
}
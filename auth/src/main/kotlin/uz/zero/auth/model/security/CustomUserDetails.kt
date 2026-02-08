package uz.zero.auth.model.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    private val id: Long,
    private val phoneNumber: String,
    private val password: String,
    private val organizationId: Long?,
    private val authorities: Set<GrantedAuthority>,
    private val enabled: Boolean
) : UserDetails {

    override fun getAuthorities() = authorities

    override fun getPassword() = password

    override fun getUsername() = phoneNumber

    override fun isEnabled() = enabled

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true

    fun getUserId() = id
    fun getOrganizationId() = organizationId
}

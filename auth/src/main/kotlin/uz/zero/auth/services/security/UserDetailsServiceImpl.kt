package uz.zero.auth.services.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

import uz.zero.auth.model.security.CustomUserDetails
import uz.zero.auth.repositories.UserRepository


@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(phoneNum: String): UserDetails {
        val user = userRepository.findByPhoneNumAndDeletedFalse(phoneNum)
            ?: throw UsernameNotFoundException("User not found with phone number: $phoneNum")

        val authorities = setOf(
            SimpleGrantedAuthority(user.role.code)
        )

        return CustomUserDetails(
            id = user.id!!,
            phoneNumber = user.phoneNum,
            password = user.password,
            authorities = authorities,
            enabled = user.active
        )
    }
}

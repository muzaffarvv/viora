package uz.zero.auth.components.loaders

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uz.zero.auth.entities.User
import uz.zero.auth.repositories.UserRepository
import uz.zero.auth.services.RoleService

@Component
class DevUserLoader(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val roleService: RoleService
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun run(vararg args: String?) {
        createRoles()
        createDevUser()
    }

    private fun createRoles() {
        val roles = listOf(
            "DEVELOPER" to "Developer",
            "ADMIN" to "Administrator",
            "MODERATOR" to "Moderator",
            "USER" to "User",
        )
        roles.forEach { (code, name) ->
            val r = roleService.createIfNotExist(name, code)
            logger.info("Ensured role exists: ${r.name} (${r.code})")
        }
    }

    private fun createDevUser() {
        val role = roleService.getByCode("ADMIN")

        val phoneNum = "998901234567"
        if (userRepository.findByPhoneNumAndDeletedFalse(phoneNum) != null) {
            logger.info("Dev user with phone $phoneNum already exists, skipping creation.")
            return
        }

        val user = User(
            firstName = "TestUser",
            lastName = "User",
            phoneNum = phoneNum,
            password = passwordEncoder.encode("Pass#2026"),
            role = role
        )

        userRepository.save(user)
        logger.info("Created dev user '${user.firstName} ${user.lastName}' with role '${role.name}'")
    }
}
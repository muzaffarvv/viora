package uz.zero.auth.services

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import uz.zero.auth.entities.Role
import uz.zero.auth.exceptions.RoleNotFoundException
import uz.zero.auth.repositories.RoleRepository

@Service
class RoleService(private val roleRepo: RoleRepository) {

    @Transactional
    fun createIfNotExist(name: String, code: String): Role =
        roleRepo.findByCode(code) ?: roleRepo.save(
            Role(name = name, code = code)
        )

    fun getByCode(code: String): Role =
        roleRepo.findByCode(code)
            ?: throw RoleNotFoundException("Role not found with code: $code")
}
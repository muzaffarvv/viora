package uz.zero.auth.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import uz.zero.auth.entities.Role

@Repository
interface RoleRepository: JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    fun findByCode(code: String): Role?
}
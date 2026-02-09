package uz.zero.auth.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "organization-service")
interface OrganizationClient {

    @GetMapping("/api/employees/{userId}/active-organization")
    fun getActiveOrganization(@PathVariable userId: Long): OrgAndEmp
}


data class OrgAndEmp (
    val employeeId: Long,
    val organizationId: Long
)

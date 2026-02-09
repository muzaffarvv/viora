package uz.zero.organizationservice

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "auth-service", url ="http://localhost:8081",configuration = [FeignOAuth2TokenConfig::class])
interface UserClient{

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): UserResponse

//    @PostMapping("/api/users/batch")
//    fun getUsersByIds(@RequestBody dto: UserBatchRequest): List<UserResponse>

    @PostMapping("/set-org")
    fun changeCurrentOrg(@RequestBody changeCurrentOrganizationRequest: ChangeCurrentOrganizationRequest)

}
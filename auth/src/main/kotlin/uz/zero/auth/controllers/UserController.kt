package uz.zero.auth.controllers

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import uz.zero.auth.model.requests.UserCreateRequest
import uz.zero.auth.model.requests.UserUpdateRequest
import uz.zero.auth.model.responses.UserResponse
import uz.zero.auth.services.AuthService
import uz.zero.auth.services.UserServiceImpl

@RestController
@RequestMapping()
class UserController(
    private val userServiceImpl: UserServiceImpl,
    private val authService: AuthService
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody @Valid request: UserCreateRequest): Long {
        return authService.register(request)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): UserResponse {
        return userServiceImpl.getById(id)
    }

    @GetMapping("/all")
    fun getAllUsers(): List<UserResponse> {
        return userServiceImpl.getAll()
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody request: UserUpdateRequest
    ): UserResponse {
        return userServiceImpl.update(id, request)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): Boolean {
        return userServiceImpl.delete(id)
    }
}
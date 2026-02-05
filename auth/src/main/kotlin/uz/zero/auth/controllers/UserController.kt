package uz.zero.auth.controllers

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import uz.zero.auth.model.requests.UserCreateRequest
import uz.zero.auth.model.requests.UserUpdateRequest
import uz.zero.auth.model.responses.UserInfoResponse
import uz.zero.auth.model.responses.UserResponse
import uz.zero.auth.services.UserService
import java.math.BigDecimal

@RestController
@RequestMapping("user")
class UserController(
    private val userService: UserService
) {

}
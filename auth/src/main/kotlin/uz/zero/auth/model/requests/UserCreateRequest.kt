package uz.zero.auth.model.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import uz.zero.auth.utils.NotSpace

data class UserCreateRequest(

    @field:Size(min = 2, max = 72)
    @field:NotSpace
    val firstName: String,

    @field:Size(max = 60)
    val lastName: String?,

    @field:Size(min = 7, max = 32)
    @field:NotSpace
    val phoneNum: String,

    @field:Size(min = 6)
    @field:Pattern.List(
        value = [
            Pattern(regexp = ".*[a-z].*", message = "PASSWORD_LOWER_CASE_ERROR"),
            Pattern(regexp = ".*[A-Z].*", message = "PASSWORD_UPPER_CASE_ERROR"),
            Pattern(regexp = ".*\\d.*", message = "PASSWORD_DIGIT_ERROR"),
            Pattern(
                regexp = ".*[~!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*",
                message = "PASSWORD_EXTRA_CHARACTER_ERROR"
            )
        ]
    )
    val password: String,

    @NotBlank(message = "confirm password is required")
    val confirmPassword: String
)

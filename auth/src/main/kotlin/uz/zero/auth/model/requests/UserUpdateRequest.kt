package uz.zero.auth.model.requests

import jakarta.validation.constraints.Size
import uz.zero.auth.utils.NotSpace

data class UserUpdateRequest(

    @field:Size(min = 2, max = 72)
    @field:NotSpace
    val firstName: String?,

    @field:Size(max = 60)
    val lastName: String?,

)

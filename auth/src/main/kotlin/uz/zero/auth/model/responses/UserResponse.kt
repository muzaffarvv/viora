package uz.zero.auth.model.responses

data class UserResponse(
    val id: Long,
    val firstName: String,
    val lastName: String?,
    val phoneNum: String,
    val role: String
)

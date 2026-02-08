package uz.zero.auth.model.responses

import java.util.Date

data class UserInfoResponse(
    val id: Long,
    val firstName: String,
    val lastName: String?,
    val phoneNum: String,
    val role: String,
    val orgId: Long?,
    val createdAt: Date?,
    val updatedAt: Long?,
    val active: Boolean,
    val deleted: Boolean
)


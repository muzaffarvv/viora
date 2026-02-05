package uz.zero.auth.model.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserInfoResponse(
    val id: Long,
    val firstName: String,
    val lastName: String?,
    val phoneNum: String,
    val role: String
)

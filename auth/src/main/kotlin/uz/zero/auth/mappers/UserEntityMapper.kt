package uz.zero.auth.mappers

import org.springframework.stereotype.Component
import uz.zero.auth.entities.User
import uz.zero.auth.model.requests.UserCreateRequest
import uz.zero.auth.model.requests.UserUpdateRequest
import uz.zero.auth.model.responses.UserResponse
import uz.zero.auth.model.responses.UserInfoResponse
import uz.zero.auth.entities.Role

@Component
class UserEntityMapper {

    fun toEntity(request: UserCreateRequest, role: Role): User {
        return User(
            firstName = request.firstName,
            lastName = request.lastName ?: "",
            phoneNum = request.phoneNum,
            password = request.password,
            role = role
        )
    }

    fun updateEntity(user: User, request: UserUpdateRequest): User {
        user.firstName = request.firstName ?: user.firstName
        user.lastName = request.lastName ?: user.lastName
        return user
    }

    fun toUserResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id!!,
            firstName = user.firstName,
            lastName = user.lastName,
            phoneNum = user.phoneNum,
            role = user.role.name
        )
    }

    fun toUserInfoResponse(user: User): UserInfoResponse {
        return UserInfoResponse(
            id = user.id!!,
            firstName = user.firstName,
            lastName = user.lastName,
            phoneNum = user.phoneNum,
            role = user.role.code,
            createdAt = user.createdDate,
            updatedAt = user.lastModifiedBy,
            active = user.active,
            deleted = user.deleted
        )
    }
}

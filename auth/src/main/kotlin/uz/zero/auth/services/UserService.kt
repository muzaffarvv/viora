package uz.zero.auth.services

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service
import uz.zero.auth.model.requests.UserCreateRequest
import uz.zero.auth.model.requests.UserUpdateRequest
import uz.zero.auth.model.responses.UserResponse
import uz.zero.auth.entities.User
import uz.zero.auth.exceptions.PasswordMismatchException
import uz.zero.auth.exceptions.PhoneNumberAlreadyTakenException
import uz.zero.auth.exceptions.UserNotFoundException
import uz.zero.auth.mappers.UserEntityMapper
import uz.zero.auth.model.responses.UserInfoResponse
import uz.zero.auth.repositories.UserRepository
import uz.zero.auth.utils.userId

interface UserService {
    fun create(createDto: UserCreateRequest): Long // return user_id
    fun getById(id: Long): UserResponse?
    fun getUserById(id: Long): User
    fun getAll(): List<UserResponse>
    fun update(id: Long, updateDto: UserUpdateRequest): UserResponse
    fun delete(id: Long): Boolean

    fun checkPhoneNumber(phoneNum: String): Boolean
    fun getByPhoneNumber(phoneNum: String): UserResponse? // for admin
}

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val roleService: RoleService,
    private val userMapper: UserEntityMapper
) : UserService {

    override fun create(createDto: UserCreateRequest): Long {
        val role = roleService.getByCode("USER")
        val user = userMapper.toEntity(createDto, role)
        // password hash -> AuthService
        val savedUser = userRepository.save(user)
        return savedUser.id!!
    }

    override fun getById(id: Long): UserResponse {
        val user = getUserById(id)
        return userMapper.toUserResponse(user)
    }

    override fun getUserById(id: Long): User {
        return userRepository.findById(id).orElseThrow { UserNotFoundException(id) }
    }

    override fun getAll(): List<UserResponse> {
        return userRepository.findAll().map { userMapper.toUserResponse(it) }
    }

    override fun update(id: Long, updateDto: UserUpdateRequest): UserResponse {
        val user = getUserById(id)
        userMapper.updateEntity(user, updateDto)
        return userMapper.toUserResponse(userRepository.save(user))
    }

    override fun delete(id: Long): Boolean {
        val user = getUserById(id)
        user.deleted = true
        userRepository.save(user)
        return true
    }

    override fun checkPhoneNumber(phoneNum: String): Boolean {
        return userRepository.existsByPhoneNum(phoneNum)
    }

    override fun getByPhoneNumber(phoneNum: String): UserResponse? {
        val user = userRepository.findByPhoneNumAndDeletedFalse(phoneNum)
            ?: throw UserNotFoundException(phoneNum)
        return userMapper.toUserResponse(user)
    }

    fun profile(): UserInfoResponse {
        val currentUserId = userId()
        val user = getUserById(currentUserId)
        return userMapper.toUserInfoResponse(user)
    }

//    fun getUserEntityByPhone(phoneNum: String): User {
//        return userRepository.findByPhoneNumAndDeletedFalse(phoneNum)
//            ?: throw UserNotFoundException("User with phone number $phoneNum not found")
//    }
}

@Service
class AuthService(
    private val userService: UserServiceImpl,
    private val passwordEncoder: PasswordEncoder
) {

    fun register(request: UserCreateRequest): Long {
        if (userService.checkPhoneNumber(request.phoneNum)) {
            throw PhoneNumberAlreadyTakenException("User with phone number ${request.phoneNum} already exists")
        }

        if (request.password != request.confirmPassword) {
            throw PasswordMismatchException("Passwords do not match")
        }

        val hashedPassword = passwordEncoder.encode(request.password)
        val userId = userService.create(request.copy(password = hashedPassword, confirmPassword = hashedPassword))
        return userId
    }
}


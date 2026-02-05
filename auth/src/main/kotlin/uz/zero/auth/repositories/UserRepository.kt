package uz.zero.auth.repositories


import uz.zero.auth.entities.User

interface UserRepository : BaseRepository<User> {
    fun findByPhoneNumAndDeletedFalse(phoneNum: String): User?
}
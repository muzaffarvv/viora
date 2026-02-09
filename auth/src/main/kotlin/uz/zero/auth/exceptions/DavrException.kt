package uz.zero.auth.exceptions

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import uz.zero.auth.model.responses.BaseMessage
import uz.zero.auth.enums.ErrorCode

sealed class DavrException : RuntimeException() {

    abstract fun errorCode(): ErrorCode

    open fun getErrorMessageArguments(): Array<Any?>? = null

    fun getErrorMessage(errorMessageSource: ResourceBundleMessageSource): BaseMessage {
        val messageKey = errorCode().name
        val errorMessage = try {
            errorMessageSource.getMessage(messageKey, getErrorMessageArguments(), LocaleContextHolder.getLocale())
        } catch (e: Exception) {
            "Message not found for key: $messageKey"
        }
        return BaseMessage(errorCode().code, errorMessage)
    }
}
class UserNotFoundException(private vararg val args: Any?) : DavrException() {

    override fun errorCode(): ErrorCode = ErrorCode.USER_NOT_FOUND

    override fun getErrorMessageArguments(): Array<Any?> = args.toList().toTypedArray()
}

class RoleNotFoundException(private vararg val args: Any?) : DavrException() {

    override fun errorCode(): ErrorCode = ErrorCode.ROLE_NOT_FOUND

    override fun getErrorMessageArguments(): Array<Any?> = args.toList().toTypedArray()
}


class PhoneNumberAlreadyTakenException(private vararg val args: Any?) : DavrException() {

    override fun errorCode(): ErrorCode = ErrorCode.PHONE_NUMBER_ALREADY_EXISTS

    override fun getErrorMessageArguments(): Array<Any?> = args.toList().toTypedArray()
}

class PasswordMismatchException(private vararg val args: Any?) : DavrException() {

    override fun errorCode(): ErrorCode = ErrorCode.PASSWORD_MISMATCH

    override fun getErrorMessageArguments(): Array<Any?> = args.toList().toTypedArray()
}

class InvalidPasswordException(private vararg val args: Any?) : DavrException() {

    override fun errorCode(): ErrorCode = ErrorCode.INVALID_PASSWORD

    override fun getErrorMessageArguments(): Array<Any?> = args.toList().toTypedArray()
}

class InvalidCredentials(private vararg val args: Any?) : DavrException() {

    override fun errorCode(): ErrorCode = ErrorCode.INVALID_PASSWORD

    override fun getErrorMessageArguments(): Array<Any?> = args.toList().toTypedArray()
}
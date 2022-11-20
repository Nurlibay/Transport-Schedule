package uz.nurlibaydev.transportschedule.utils

sealed class ResultData<out T> {
    data class Success<T>(val data: T) : ResultData<T>()
    data class Error<T>(val error: Throwable) : ResultData<T>()
    data class Message<T>(val message: String) : ResultData<T>()

    val isSuccess = this is Success<T>
    val isError = this is Error
    val isMessage = this is Message


    inline fun onSuccess(block: (T) -> Unit): ResultData<T> {
        if (this is Success<T>) block(this.data)
        return this
    }

    inline fun onError(block: (Throwable) -> Unit): ResultData<T> {
        if (this is Error) block(this.error)
        return this
    }

    inline fun onMessage(block: (String) -> Unit): ResultData<T> {
        if (this is Message) block(this.message)
        return this
    }

    companion object {
        fun <T> success(value: T) = Success(value)
        fun <T> error(value: Throwable) = Error<T>(value)
        fun <T> message(value: String) = Message<T>(value)
    }
}


package com.rohitpandey.domain.models

sealed class Result<out T> {
    data object Loading : Result<Nothing>()

    data class Success<out T>(val data: T) : Result<T>()

    data class Error(
        val exception: Throwable,
        val message: String? = exception.message,
    ) : Result<Nothing>()

    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val isError:   Boolean get() = this is Error

    fun getOrNull(): T? = (this as? Success)?.data

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error   -> throw exception
        is Loading -> error("Result is still Loading — cannot unwrap.")
    }

    inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
        is Loading -> Loading
        is Success -> Success(transform(data))
        is Error   -> this
    }

    inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun <T> Result<T>.onError(action: (Throwable) -> Unit): Result<T> {
        if (this is Error) action(exception)
        return this
    }

    inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
        if (this is Loading) action()
        return this
    }

    suspend fun <T> runCatchingResult(block: suspend () -> T): Result<T> = try {
        Success(block())
    } catch (e: Exception) {
        Error(e)
    }
}

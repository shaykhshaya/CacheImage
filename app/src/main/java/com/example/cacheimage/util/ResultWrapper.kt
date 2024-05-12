package com.example.cacheimage.util

sealed class ApiResultWrapper<out T> {
    data class Success<out T>(val data: T) : ApiResultWrapper<T>()
    data class GenericError(val code: Int?, val message: String?) : ApiResultWrapper<Nothing>()
    data class NetworkError(val exception: Exception) : ApiResultWrapper<Nothing>()
    data class Error<out T>(val throwable: Throwable, val data: T) : ApiResultWrapper<T>()
    data class OnAppError<out T>(val code: String?, val message: String?) : ApiResultWrapper<T>()
    data class OnResult<out T>(val data: T,val code: String?, val message: String?) : ApiResultWrapper<T>()
    object Loading : ApiResultWrapper<Nothing>()

    data class OnValidationError<out T>(val code: String?,val validationMessages : ArrayList<String> ? = null): ApiResultWrapper<T>()


}
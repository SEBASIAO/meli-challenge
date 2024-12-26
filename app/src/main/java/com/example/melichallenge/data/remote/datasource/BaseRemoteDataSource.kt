package com.example.melichallenge.data.remote.datasource

import android.accounts.NetworkErrorException
import retrofit2.Response
import java.net.UnknownHostException

abstract class BaseRemoteDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            return if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error(response.message())
            }
        } catch (e : Exception){
            return if (e is UnknownHostException)
                Resource.error("No internet connection")
            else Resource.error("Network Call Error: ${e.message.orEmpty()}")
        }

    }
}

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
    }

    companion object {
        fun <T> success(data: T?) = Resource(Status.SUCCESS, data, null)

        fun <T> error(message: String, data: T? = null) = Resource(Status.ERROR, data, message)
    }
}

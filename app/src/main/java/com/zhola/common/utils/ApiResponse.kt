package io.kliks.utils

import okhttp3.Headers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface ApiResponse {
    fun onProgressStart(startProgress: Boolean)
    fun onApiSuccess(
        responseCode: Int,
        responseMessage: String,
        response: String?,
        responseHeaders: Headers?
    )

    fun onAPiFailure(
        errorCode: Int,
        t: Throwable?,
        response: Response<String>?,
        call: Call<String>?,
        callBack: Callback<String>?
    )

    fun onProgressStop()
}
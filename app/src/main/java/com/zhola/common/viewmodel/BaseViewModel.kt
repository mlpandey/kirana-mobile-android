package com.zhola.common.viewmodel

import androidx.lifecycle.ViewModel
import com.zhola.common.activity.BaseActivity
import io.kliks.utils.ApiResponse
import okhttp3.Headers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseViewModel : ViewModel(), ApiResponse {

    lateinit var baseActivity: BaseActivity

    fun setData(baseActivity: BaseActivity) {
        this.baseActivity = baseActivity
    }

    override fun onProgressStart(startProgress: Boolean) {
        baseActivity.onProgressStart(startProgress)
    }

    override fun onApiSuccess(
        responseCode: Int,
        responseMessage: String,
        response: String?,
        responseHeaders: Headers?
    ) {

    }

    override fun onAPiFailure(
        errorCode: Int,
        t: Throwable?,
        response: Response<String>?,
        call: Call<String>?,
        callBack: Callback<String>?
    ) {
        baseActivity.onAPiFailure(errorCode, t, response, call, callBack)
    }

    override fun onProgressStop() {
        baseActivity.onProgressStop()
    }
}
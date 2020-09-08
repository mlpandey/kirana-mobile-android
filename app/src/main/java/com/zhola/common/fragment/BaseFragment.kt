package com.zhola.common.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.zhola.common.activity.BaseActivity
import com.zhola.common.utils.Const
import io.kliks.utils.ApiResponse
import io.kliks.utils.ApiService
import io.kliks.utils.RetrofitClient
import okhttp3.Headers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseFragment : Fragment(),ApiResponse {
    var baseActivity: BaseActivity? = null
    var retrofitClient: RetrofitClient? = null
    var apiService: ApiService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseActivity = activity as BaseActivity?

        if (baseActivity!!.retrofitClient == null) {
            baseActivity!!.retrofitClient = RetrofitClient.getInstance(baseActivity!!)
            baseActivity!!.retrofit = baseActivity!!.retrofitClient!!.getClient(Const.API_BASE_URL)
            baseActivity!!.apiService = baseActivity!!.retrofit!!.create(ApiService::class.java)
        }
        retrofitClient = baseActivity!!.retrofitClient
        apiService = baseActivity!!.apiService
    }

    override fun onProgressStart(startProgress: Boolean) {
        baseActivity!!.onProgressStart(startProgress)
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
        baseActivity!!.onAPiFailure(errorCode, t, response, call, callBack)
    }

    override fun onProgressStop() {
        baseActivity!!.onProgressStop()
    }
}
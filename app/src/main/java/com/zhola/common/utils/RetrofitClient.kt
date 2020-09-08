package io.kliks.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.zhola.BuildConfig
import com.zhola.R
import com.zhola.common.utils.OnUpgradeDialogClick
import kotlinx.android.synthetic.main.dialog_common.view.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class RetrofitClient private constructor(context: Context) {
    private var retrofit: Retrofit? = null
    private val mContext: Context = context

    companion object : SingletonHolder<RetrofitClient, Context>(::RetrofitClient)

    fun getClient(baseUrl: String?): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkhttpClient().getUnsafeOkHttpClient(mContext))
                .build()
        }
        return retrofit
    }

    fun setLoginStatus(token: String?) {
        val pref = PreferenceManager.getDefaultSharedPreferences(mContext)
        val editor = pref.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun getLoginStatus(): String? {
        val pref = PreferenceManager.getDefaultSharedPreferences(mContext)
        var token: String? = null
        if (pref.contains("token")) {
            token = pref.getString("token", null)
        }
        return token
    }

    fun sendRequest(call: Call<String>, apiResponse: ApiResponse, showProgress: Boolean) {
        if (BuildConfig.DEBUG) {
            Log.e("URL", call.request().url().toString())
        }
        if (!(NetworkUtil.getConnectivityStatus(mContext) == NetworkUtil.TYPE_IS_CONNECTING || NetworkUtil.getConnectivityStatus(
                mContext
            ) == NetworkUtil.TYPE_NOT_CONNECTED)
        ) {
            apiResponse.onProgressStart(showProgress)
            call.enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    apiResponse.onProgressStop()
                    apiResponse.onAPiFailure(0, t, null, call, this)
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    apiResponse.onProgressStop()
                    if (response.isSuccessful) {
                        if (BuildConfig.DEBUG) {
                            Log.e("Response", response.body().toString())
                        }
                        apiResponse.onApiSuccess(
                            response.code(),
                            response.message(),
                            response.body(),
                            response.headers()
                        )
                    } else {
                        apiResponse.onAPiFailure(response.code(), null, response, call, this)
                    }
                }

            })
        } else {
            noInternetAlertBox("No Internet Connection")
        }
    }

    private fun noInternetAlertBox(
        msg: String
    ) {
        val mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_common, null)
        val mBuilder = AlertDialog.Builder(mContext).setView(mDialogView)
        mDialogView.txt_error.text = msg
        mBuilder.setCancelable(false)

        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mDialogView.txt_postive.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }
}
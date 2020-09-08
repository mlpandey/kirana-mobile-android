package io.kliks.utils

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.zhola.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class OkhttpClient {

    var context: Context? = null

    fun getUnsafeOkHttpClient(mContext: Context): OkHttpClient {
        context = mContext
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }


                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.followRedirects(false)
            builder.followSslRedirects(false)
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.apply { this.level = HttpLoggingInterceptor.Level.BODY }
            builder.addInterceptor { chain ->
                var request = chain.request()
                if (getLoginStatus() != null) {
                    request = request.newBuilder()
                        .addHeader(
                            "Authorization", "" + getLoginStatus()
                        )
////                        .addHeader(
////                            "Accept-Language",
////                            preferenceHelper.getKeyValue(PreferenceConstants.LANGUAGE_TYPE)
////                        )
//                        .addHeader("access_Token", getLoginStatus()!!)
                        .build()
                }

                chain.proceed(request)
            }

            if (BuildConfig.DEBUG) {
                builder.interceptors().add(loggingInterceptor)
            }
            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun getLoginStatus(): String? {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        var token: String? = null
        if (pref.contains("token")) {
            token = pref.getString("token", null)
        }
        return token
    }
}
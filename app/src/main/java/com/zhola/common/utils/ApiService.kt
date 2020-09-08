package io.kliks.utils

import com.google.gson.JsonObject
import com.zhola.common.utils.Const
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @POST(Const.API_SIGN_UP)
    fun hitSignUpApi(@Body jsonObject: JsonObject): Call<String>

    @POST(Const.API_OTP)
    fun hitOtpApi(@Body jsonObject: JsonObject): Call<String>

    @POST(Const.API_OTP_VALIDATE)
    fun hitOtpValidateApi(@Body jsonObject: JsonObject): Call<String>

    @POST(Const.API_LOGIN)
    fun hitLoginApi(@Body jsonObject: JsonObject): Call<String>

    @GET(Const.API_GET_PROFILE)
    fun hitGetProfileApi(): Call<String>


//    @GET(Const.API_GET_TRIPS + "/{id}")
//    fun hitTripDetailApi(@Path("id") id: String): Call<String>
}
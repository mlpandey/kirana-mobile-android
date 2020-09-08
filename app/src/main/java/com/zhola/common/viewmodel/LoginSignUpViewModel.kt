package com.zhola.common.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.zhola.R
import com.zhola.common.extensions.replaceFragWithArgs
import com.zhola.common.fragment.CongratulationsFragment
import com.zhola.common.fragment.OtpFragment
import com.zhola.common.model.SignUpData
import com.zhola.common.utils.Const
import okhttp3.Headers
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LoginSignUpViewModel : BaseViewModel() {

    private var role: String?=null
    private var url: String? = ""
    private var signUpData: SignUpData? = null
    private var fromLogin: Boolean = false
    private var fromOtp: Boolean = false
    var otpSent = MutableLiveData<Boolean>()

    //hit login api
    fun hitLoginApi(signUpData: SignUpData, fromLogin: Boolean) {
        this.fromLogin = fromLogin
        this.signUpData = signUpData
        url = Const.API_LOGIN
        val jsonObject = JsonObject()
        jsonObject.addProperty("username", signUpData.email)
        jsonObject.addProperty("password", signUpData.password)
        val call = baseActivity.apiService!!.hitLoginApi(jsonObject)
        baseActivity.retrofitClient!!.sendRequest(call, this, true)
    }

    // hit register api based on role
    fun hitRegisterApi(signUpData: SignUpData) {
        this.signUpData = signUpData
        url = Const.API_SIGN_UP
        val jsonObject = JsonObject()
        jsonObject.addProperty("email", signUpData.email)
        jsonObject.addProperty("password", signUpData.password)
        jsonObject.addProperty("role", signUpData.role)
        if (signUpData.role == Const.ROLE_CUSTOMER) {
            val customerJson = JsonObject()
            customerJson.addProperty("name", signUpData.name)
            customerJson.addProperty("cell", signUpData.phoneNumber)
            customerJson.addProperty(
                "address1",
                signUpData.addressLineOne
            )
            customerJson.addProperty(
                "address2",
                signUpData.addressLineTwo
            )
            customerJson.addProperty("city", signUpData.city)
            customerJson.addProperty("state", signUpData.state)
            customerJson.addProperty("zip", signUpData.zipCode)
            customerJson.addProperty("country", signUpData.country)
            if (!signUpData.profileImg.isNullOrEmpty()) {
                customerJson.addProperty(
                    "profilePicture",
                    baseActivity.getFileToByte(signUpData.profileImg)
                )
            }
            jsonObject.add(signUpData.role, customerJson)
        } else if (signUpData.role == Const.ROLE_VENDOR) {
            val vendorJson = JsonObject()
            vendorJson.addProperty("name", signUpData.name)
            vendorJson.addProperty("cell", signUpData.phoneNumber)
            vendorJson.addProperty(
                "businessName",
                signUpData.businessName
            )
            vendorJson.addProperty(
                "address1",
                signUpData.addressLineOne
            )
            vendorJson.addProperty(
                "address2",
                signUpData.addressLineTwo
            )
            vendorJson.addProperty("businessPhone", signUpData.businessPhone)
            vendorJson.addProperty("businessEmail ", signUpData.businessEmail)
            vendorJson.addProperty("city", signUpData.city)
            vendorJson.addProperty("state", signUpData.state)
            vendorJson.addProperty("zip", signUpData.zipCode)
            vendorJson.addProperty("country", signUpData.country)
            vendorJson.addProperty("companyType", signUpData.companyType)
            vendorJson.addProperty("businessCategory", signUpData.businessCategory)
            vendorJson.addProperty("canDeliver", signUpData.canDeliver.toString())
            vendorJson.addProperty("isMerchandiseDigital", signUpData.isMerchantDigital.toString())
            vendorJson.addProperty(
                "hasInventoryManagementSystem",
                signUpData.haveInventoryManagement.toString()
            )
            vendorJson.addProperty("gstNumber", signUpData.gstNumber)
            vendorJson.addProperty(
                "gstCertPicture",
                baseActivity.getFileToByte(signUpData.gstDocument)
            )
            vendorJson.addProperty("panNumber", signUpData.panNumber)
            vendorJson.addProperty(
                "panCertPicture",
                baseActivity.getFileToByte(signUpData.panNumber)
            )
            if (!signUpData.businessProfile.isNullOrEmpty()) {
                vendorJson.addProperty(
                    "profilePicture",
                    baseActivity.getFileToByte(signUpData.businessProfile)
                )
            }

            jsonObject.add(signUpData.role, vendorJson)
        } else {
            val driverJson = JsonObject()
            driverJson.addProperty("name", signUpData.name)
            driverJson.addProperty("cell", signUpData.phoneNumber)
            driverJson.addProperty("vechicleNumber", signUpData.vehicleNumber)
            driverJson.addProperty(
                "drivingLicenseNumber",
                signUpData.driverLicenceNumber
            )

            if (!signUpData.drivingDocument.isNullOrEmpty()) {
                driverJson.addProperty(
                    "drivingLicensePicture",
                    baseActivity.getFileToByte(signUpData.drivingDocument)
                )
            }
            jsonObject.add(signUpData.role, driverJson)
        }
        val call = baseActivity.apiService!!.hitSignUpApi(jsonObject)
        baseActivity.retrofitClient!!.sendRequest(call, this, true)
    }

    // hit api to initiate otp
    fun hitOtpApi(signUpData: SignUpData, fromOtp: Boolean) {
        this.fromOtp = fromOtp
        this.signUpData = signUpData
        val jsonObject = JsonObject()
        jsonObject.addProperty("phone", this.signUpData!!.phoneNumber)
        url = Const.API_OTP
        val call = baseActivity.apiService!!.hitOtpApi(jsonObject)
        baseActivity.retrofitClient!!.sendRequest(call, this, true)
    }

    // api to check that otp is valid
    fun hitOtpValidateApi(signUpData: SignUpData, otpValue: String) {
        this.signUpData = signUpData
        url = Const.API_OTP_VALIDATE
        val jsonObject = JsonObject()
        jsonObject.addProperty("phone", signUpData.phoneNumber)
        jsonObject.addProperty("otp", otpValue)
        val call = baseActivity.apiService!!.hitOtpValidateApi(jsonObject)
        baseActivity.retrofitClient!!.sendRequest(call, this, true)
    }


    // api success response
    override fun onApiSuccess(
        responseCode: Int,
        responseMessage: String,
        response: String?,
        responseHeaders: Headers?
    ) {
        super.onApiSuccess(responseCode, responseMessage, response, responseHeaders)
        val jsonObject = JSONObject(response)

        if (url == Const.API_LOGIN) {

            if (jsonObject.has("success")) {
                val success = jsonObject.getBoolean("success")
                if (success) {

                    role = jsonObject.getString("role")
                baseActivity.prefStore!!.saveString("role", role!!)


                    baseActivity.retrofitClient!!.setLoginStatus(
                        responseHeaders!!.get("Authorization").toString()
                    )
//                    hitGetProfileApi()
                    if (fromLogin) {
                        if (role == "CUSTOMER") {
                            baseActivity.gotoCustomerMainActivity()
                        } else if (role == "VENDOR") {
                            baseActivity.gotoBusinessMainActivity()
                        } else if (role == "DRIVER") {
                            baseActivity.gotoDriverMainActivity()
                        }
                    } else {
                        var bundle: Bundle? = null
                        if (role == "CUSTOMER") {
                            bundle = Bundle()
                            bundle.putInt("roleId", Const.TYPE_CUSTOMER)
                        } else if (role == "VENDOR") {
                            bundle = Bundle()
                            bundle.putInt("roleId", Const.TYPE_VENDOR)
                        } else if (role == "DRIVER") {
                            bundle = Bundle()
                            bundle.putInt("roleId", Const.TYPE_DRIVER)
                        }
                        baseActivity.replaceFragWithArgs(
                            CongratulationsFragment(),
                            R.id.login_frame,
                            bundle!!
                        )
                    }
                } else {
                    if (jsonObject.has("message"))
                        baseActivity.showToast(jsonObject.getString("message"))
                }
            } else {
                baseActivity.retrofitClient!!.setLoginStatus(
                    responseHeaders!!.get("Authorization").toString()
                )
//                hitGetProfileApi()
                if (fromLogin) {
                    if (role == "CUSTOMER") {
                        baseActivity.gotoCustomerMainActivity()
                    } else if (role == "VENDOR") {
                        baseActivity.gotoBusinessMainActivity()
                    } else if (role == "DRIVER") {
                        baseActivity.gotoDriverMainActivity()
                    }
                } else {
                    var bundle: Bundle? = null
                    if (role == "CUSTOMER") {
                        bundle = Bundle()
                        bundle.putInt("roleId", Const.TYPE_CUSTOMER)
                    } else if (role == "VENDOR") {
                        bundle = Bundle()
                        bundle.putInt("roleId", Const.TYPE_VENDOR)
                    } else if (role == "DRIVER") {
                        bundle = Bundle()
                        bundle.putInt("roleId", Const.TYPE_DRIVER)
                    }
                    baseActivity.replaceFragWithArgs(
                        CongratulationsFragment(),
                        R.id.login_frame,
                        bundle!!
                    )
                }
            }
        } else {
            var success = false
            if (jsonObject.has("success"))
                success = jsonObject.getBoolean("success")

            if (success) {
                when (url) {
                    Const.API_SIGN_UP -> {
                        hitLoginApi(signUpData!!, false)
                    }
                    Const.API_OTP -> {
                        if (!fromOtp) {
                            val bundle = Bundle()
                            bundle.putInt("roleId", Const.TYPE_DRIVER)
                            bundle.putParcelable("signUpData", signUpData)
                            baseActivity.replaceFragWithArgs(
                                OtpFragment(),
                                R.id.login_frame,
                                bundle
                            )
                        } else {
                            otpSent.value = true
                            baseActivity.showToast("OTP sent successfully")
                        }
                    }
                    Const.API_OTP_VALIDATE -> {
                        hitRegisterApi(signUpData!!)
                    }

                    Const.API_GET_PROFILE -> {
                        //                    if (fromLogin) {
//                        if (role == "CUSTOMER") {
//
//                            baseActivity.gotoCustomerMainActivity()
//                        } else if (role == "VENDOR") {
//                            baseActivity.gotoBusinessMainActivity()
//                        } else if (role == "DRIVER") {
//                            baseActivity.gotoDriverMainActivity()
//                        }
//                    } else {
//                        if (role == "CUSTOMER") {
//                            val bundle = Bundle()
//                            bundle.putInt("roleId", Const.TYPE_CUSTOMER)
//                            baseActivity.replaceFragWithArgs(
//                                CongratulationsFragment(),
//                                R.id.container,
//                                bundle
//                            )
//                        } else if (role == "VENDOR") {
//                            val bundle = Bundle()
//                            bundle.putInt("roleId", Const.TYPE_VENDOR)
//                            baseActivity.replaceFragWithArgs(
//                                CongratulationsFragment(),
//                                R.id.business_container,
//                                bundle
//                            )
//                        } else if (role == "DRIVER") {
//                            val bundle = Bundle()
//                            bundle.putInt("roleId", Const.TYPE_DRIVER)
//                            baseActivity.replaceFragWithArgs(
//                                CongratulationsFragment(),
//                                R.id.driver_container,
//                                bundle
//                            )
//                        }
//                    }
                    }
                }
            } else {
                if (jsonObject.has("message")) {
                    baseActivity.showToast(jsonObject.getString("message"))
                }
            }
        }
    }

    // api to get profile
    fun hitGetProfileApi() {
        url = Const.API_GET_PROFILE
        val call = baseActivity.apiService!!.hitGetProfileApi()
        baseActivity.retrofitClient!!.sendRequest(call, this, true)
    }

    // on Api failure or error
    override fun onAPiFailure(
        errorCode: Int,
        t: Throwable?,
        response: Response<String>?,
        call: Call<String>?,
        callBack: Callback<String>?
    ) {
        if (url == Const.API_LOGIN) {
            val header = response!!.headers().get("Authorization")
            if (header == null)
                baseActivity.showToast(baseActivity.getString(R.string.something_went_wrong))
        } else {
            super.onAPiFailure(errorCode, t, response, call, callBack)
        }
    }
}
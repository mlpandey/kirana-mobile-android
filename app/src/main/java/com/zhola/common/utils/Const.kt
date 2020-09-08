package com.zhola.common.utils

object Const {

    val API_BASE_URL = "http://kirana-env.eba-emzm4tmx.us-west-1.elasticbeanstalk.com/"
    const val API_SIGN_UP = "/user/register/"
    const val API_OTP = "/otp/initiate"
    const val API_OTP_VALIDATE = "/otp/validate"
    const val API_LOGIN = "/login"
    const val API_GET_PROFILE = "/user/profile"
    val PERMISSION_ID = 99
    val STATUS_OK = 200

    val TYPE_DRIVER = 40
    val TYPE_CUSTOMER = 70
    val TYPE_VENDOR = 80
    val TYPE_DRIVER_ORDER = 90

    val CAMERA_CODE = 456
    val GALLERY_CODE = 564

    val GST_CAMERA_CODE = 364
    val GST_GALLERY_CODE = 123

    val PAN_CAMERA_CODE = 235
    val PAN_GALLERY_CODE = 236

    val DRIVING_CAMERA_CODE = 765
    val DRIVING_GALLERY_CODE = 567

    val IMAGE_PERMISSION = 242
    val GST_PERMISSION = 453
    val PAN_PERMISSION = 674
    val DRIVING_PERMISSION = 743


    val ROLE_CUSTOMER = "customer"
    val ROLE_VENDOR = "vendor"
    val ROLE_DRIVER = "driver"

}
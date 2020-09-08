package com.zhola.common.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class SignUpData(
    var name: String = "",
    var email: String = "",
    var phoneNumber: String = "",
    var password: String = "",
    var addressLineOne: String = "",
    var addressLineTwo: String = "",
    var city: String = "",
    var state: String = "",
    var zipCode: String = "",
    var country: String = "",
    var businessName: String = "",
    var businessPhone: String = "",
    var businessEmail: String = "",
    var businessProfile: String = "",
    var profileImg: String = "",
    var companyType: String = "",
    var businessCategory: String = "",
    var canDeliver: Boolean = false,
    var isMerchantDigital: Boolean = false,
    var haveInventoryManagement: Boolean = false,
    var gstNumber: String = "",
    var gstDocument: String = "",
    var panNumber: String = "",
    var panDocument: String = "",
    var vehicleNumber: String = "",
    var driverLicenceNumber: String = "",
    var drivingType: String = "",
    var drivingDocument: String = "",
    var role: String = ""
) : Parcelable
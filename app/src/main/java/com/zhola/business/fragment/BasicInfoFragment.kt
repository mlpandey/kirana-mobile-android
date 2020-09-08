package com.zhola.business.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.ybs.countrypicker.CountryPicker
import com.zhola.R
import com.zhola.common.activity.BaseActivity
import com.zhola.common.activity.LoginSignUpActivity
import com.zhola.common.extensions.replaceFragWithArgs
import com.zhola.common.fragment.BaseFragment
import com.zhola.common.model.SignUpData
import com.zhola.common.utils.ClickHandler
import com.zhola.common.utils.Const
import com.zhola.common.utils.ImageUtils
import com.zhola.common.utils.OnUpgradeDialogClick
import com.zhola.databinding.FragmentBasicInfoBinding


class BasicInfoFragment : BaseFragment(), ClickHandler, OnUpgradeDialogClick,
    BaseActivity.PermCallback {

    private lateinit var binding: FragmentBasicInfoBinding
    private var cameraUri: Uri? = null
    private var cameraPath: String? = null
    private var signUpData: SignUpData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            signUpData = it.getParcelable("signUpData")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_basic_info, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (baseActivity as LoginSignUpActivity).setToolbar("Business Profile", true)
        initUI()
    }

    private fun initUI() {
        binding.clickHandler = this
    }

    override fun handleClick(view: View) {
        when (view.id) {

            R.id.countryTIL, R.id.countryCV, R.id.countryET -> {
                val picker =
                    CountryPicker.newInstance("Select Country") // dialog title

                picker.setListener { name, code, dialCode, flagDrawableResID ->
                    picker.dismiss()
                    binding.countryET.setText(name)
                }
                picker.show(baseActivity!!.supportFragmentManager, "COUNTRY_PICKER")
            }

            R.id.cameraIV -> {
                if (baseActivity!!.checkPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), Const.IMAGE_PERMISSION, this
                    )
                ) {
                    cameraUri = ImageUtils.getInstance()
                        .CameraGalleryIntent(baseActivity!!, Const.CAMERA_CODE, Const.GALLERY_CODE)
                }
            }

            R.id.saveBT -> {
                if (isBasicInfoValidate()) {
                    signUpData!!.businessName = binding.businessNameET.text.toString().trim()
                    signUpData!!.businessPhone = binding.phoneNumberET.text.toString().trim()
                    signUpData!!.businessEmail = binding.emailET.text.toString().trim()
                    signUpData!!.addressLineOne = binding.addressOneET.text.toString().trim()
                    signUpData!!.addressLineTwo = binding.addressTwoET.text.toString().trim()
                    signUpData!!.city = binding.cityET.text.toString().trim()
                    signUpData!!.state = binding.stateET.text.toString().trim()
                    signUpData!!.zipCode = binding.zipCodeET.text.toString().trim()
                    signUpData!!.country = binding.countryET.text.toString().trim()
                    val bundle = Bundle()
                    bundle.putParcelable("signUpData", signUpData)
                    baseActivity!!.replaceFragWithArgs(
                        EssentialFragment(),
                        R.id.login_frame,
                        bundle
                    )
                }
            }
        }
    }

    private fun isBasicInfoValidate(): Boolean {
        /*if (cameraPath.isNullOrEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_upload_the_business_logo),
                this
            )
        } else*/ if (binding.businessNameET.text!!.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_business_name), this)
        } else if (binding.phoneNumberET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_business_phone_number),
                this
            )
        } else if (binding.emailET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_business_email_address),
                this
            )
        } else if (!baseActivity!!.isValidMail(binding.emailET.text!!.toString())) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_valid_business_email_address),
                this
            )
        } else if (binding.addressOneET.text!!.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_address_line_one),
                this
            )
        } else if (binding.addressTwoET.text!!.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_address_line_two),
                this
            )
        } else if (binding.cityET.text!!.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_city), this)
        } else if (binding.stateET.text!!.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_state), this)
        } else if (binding.zipCodeET.text!!.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_zip_code), this)
        } else if (binding.countryET.text!!.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_country), this)
        } else {
            return true
        }
        return false
    }

    override fun onUpgradeDialogClick(action: String) {

    }

    override fun permGranted(resultCode: Int) {
        if (resultCode == Const.IMAGE_PERMISSION) {
            cameraUri = ImageUtils.getInstance()
                .CameraGalleryIntent(baseActivity!!, Const.CAMERA_CODE, Const.GALLERY_CODE)
        }
    }

    override fun permDenied(resultCode: Int) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == Const.CAMERA_CODE) {
            CropImage.activity(cameraUri).setAspectRatio(1, 1).start(baseActivity!!)
        } else if (resultCode == Activity.RESULT_OK && requestCode == Const.GALLERY_CODE) {
            if (data != null) {
                CropImage.activity(data.data).setAspectRatio(1, 1).start(baseActivity!!)
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val resultUri = result.uri
                cameraPath = resultUri.path
                Glide.with(baseActivity!!).load(resultUri.path).into(binding.logoIV)
            }
        }
    }
}
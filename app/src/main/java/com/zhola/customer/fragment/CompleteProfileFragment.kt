package com.zhola.customer.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.ybs.countrypicker.CountryPicker
import com.zhola.R
import com.zhola.common.activity.BaseActivity
import com.zhola.common.activity.LoginSignUpActivity
import com.zhola.common.fragment.BaseFragment
import com.zhola.common.model.SignUpData
import com.zhola.common.utils.ClickHandler
import com.zhola.common.utils.Const
import com.zhola.common.utils.ImageUtils
import com.zhola.common.utils.OnUpgradeDialogClick
import com.zhola.common.viewmodel.LoginSignUpViewModel
import com.zhola.databinding.FragmentCompleteProfileBinding

class CompleteProfileFragment : BaseFragment(), ClickHandler, BaseActivity.PermCallback,
    OnUpgradeDialogClick {

    private lateinit var binding: FragmentCompleteProfileBinding
    private var cameraUri: Uri? = null
    private var cameraPath: String? = null
    private var signUpData: SignUpData? = null
    private var url: String? = ""
    private lateinit var mViewModel: LoginSignUpViewModel

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
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_complete_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as LoginSignUpActivity).setToolbar("Profile Detail", true)

        initUI()
    }

    private fun initUI() {
        mViewModel = ViewModelProvider(this).get(LoginSignUpViewModel::class.java)
        mViewModel.setData(baseActivity!!)

        binding.clickHandler = this
    }

    override fun handleClick(view: View) {
        when (view.id) {

            R.id.customerBT -> {
                if (isValidate()) {
                    url = Const.API_SIGN_UP
                    signUpData!!.addressLineOne = binding.addressOneET.text!!.trim().toString()
                    signUpData!!.addressLineTwo = binding.addressTwoET.text!!.trim().toString()
                    signUpData!!.city = binding.cityET.text!!.trim().toString()
                    signUpData!!.state = binding.stateET.text!!.trim().toString()
                    signUpData!!.zipCode = binding.zipCodeET.text!!.trim().toString()
                    signUpData!!.country = binding.countryET.text!!.trim().toString()
                    //signUpData!!.profileImg = cameraPath!!
                    signUpData!!.role = Const.ROLE_CUSTOMER
                    mViewModel.hitOtpApi(signUpData!!, false)
                }
            }

            R.id.cameraCIV -> {
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

            R.id.countryCV, R.id.countryET, R.id.countryTIL -> {
                val picker =
                    CountryPicker.newInstance("Select Country") // dialog title

                picker.setListener { name, code, dialCode, flagDrawableResID ->
                    picker.dismiss()
                    binding.countryET.setText(name)
                }
                picker.show(baseActivity!!.supportFragmentManager, "COUNTRY_PICKER")
            }
        }
    }

    private fun isValidate(): Boolean {
        /*if (cameraPath.isNullOrEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_select_the_profile_image),
                this
            )
        } else*/ if (binding.addressOneET.text.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_address_line_one),
                this
            )
        } else if (binding.addressTwoET.text.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_address_line_two),
                this
            )
        } else if (binding.cityET.text.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_city), this)
        } else if (binding.stateET.text.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_state), this)
        } else if (binding.zipCodeET.text.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_zip_code), this)
        } else if (binding.countryET.text.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_country), this)
        } else {
            return true
        }
        return false
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
                Glide.with(baseActivity!!).load(resultUri.path).into(binding.profileCIV)
                cameraPath = resultUri.path!!
//                val picture_path = mImageUtility.compressImage(resultUri.path!!)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.e("error", error.localizedMessage!!)
            }
        }
    }

    override fun onUpgradeDialogClick(action: String) {

    }
}
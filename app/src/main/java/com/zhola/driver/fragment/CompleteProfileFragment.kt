package com.zhola.driver.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
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
import com.zhola.databinding.FragmentDriverCompleteProfileBinding

class CompleteProfileFragment : BaseFragment(), ClickHandler, OnUpgradeDialogClick,
    BaseActivity.PermCallback {

    private lateinit var binding: FragmentDriverCompleteProfileBinding
    private var cameraUri: Uri? = null
    private var drivingCameraUri: Uri? = null
    private var cameraPath: String? = null
    private var drivingCameraPath: String? = null
    private var typeSelected: Int = 0
    private var deliveryType = -1
    private var signUpData: SignUpData? = null
    private var url: String = ""
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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_driver_complete_profile,
            container,
            false
        )
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
            R.id.saveBT -> {
                if (isValidate()) {
                    signUpData!!.vehicleNumber = binding.vehicleNumberET.text!!.trim().toString()
                    signUpData!!.driverLicenceNumber =
                        binding.licenseNumberET.text!!.trim().toString()
                    signUpData!!.drivingDocument = drivingCameraPath!!
                    signUpData!!.role = Const.ROLE_DRIVER
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
                    typeSelected = Const.IMAGE_PERMISSION
                    cameraUri = ImageUtils.getInstance()
                        .CameraGalleryIntent(baseActivity!!, Const.CAMERA_CODE, Const.GALLERY_CODE)
                }
            }

            R.id.drivingCV -> {
                if (baseActivity!!.checkPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), Const.DRIVING_PERMISSION, this
                    )
                ) {
                    typeSelected = Const.DRIVING_PERMISSION
                    drivingCameraUri = ImageUtils.getInstance()
                        .CameraGalleryIntent(
                            baseActivity!!,
                            Const.DRIVING_CAMERA_CODE,
                            Const.DRIVING_GALLERY_CODE
                        )
                }
            }

            R.id.drivingTypeCV, R.id.drivingTypeTIL, R.id.drivingTypeET -> {
                openPopupWindow(view)
            }
        }
    }

    private fun isValidate(): Boolean {
        /*if (cameraPath.isNullOrEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_select_the_profile_image),
                this
            )
        } else*/ if (binding.vehicleNumberET.text!!.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_vehicle_number),
                this
            )
        } else if (binding.licenseNumberET.text!!.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_driver_license_number),
                this
            )
        } else if (deliveryType == -1) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_select_the_delivery_type),
                this
            )
        } else if (drivingCameraPath.isNullOrEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_upload_the_image_of_driving_license),
                this
            )
        } else {
            return true
        }
        return false
    }

    override fun onUpgradeDialogClick(action: String) {

    }

    override fun permGranted(resultCode: Int) {
        if (resultCode == Const.IMAGE_PERMISSION) {
            typeSelected = Const.IMAGE_PERMISSION
            cameraUri = ImageUtils.getInstance()
                .CameraGalleryIntent(baseActivity!!, Const.CAMERA_CODE, Const.GALLERY_CODE)
        } else if (resultCode == Const.DRIVING_PERMISSION) {
            typeSelected = Const.DRIVING_PERMISSION
            drivingCameraUri = ImageUtils.getInstance()
                .CameraGalleryIntent(
                    baseActivity!!,
                    Const.DRIVING_CAMERA_CODE,
                    Const.DRIVING_GALLERY_CODE
                )
        }
    }

    override fun permDenied(resultCode: Int) {

    }

    fun openPopupWindow(buttonView: View) {
        val popUpView: View = layoutInflater.inflate(
            R.layout.driver_type_layout,
            null
        ) // inflating popup layout

        val popupWindow = PopupWindow(
            popUpView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        popupWindow.isOutsideTouchable = true
        popupWindow.animationStyle = android.R.style.Animation_Dialog
        popupWindow.showAsDropDown(buttonView, -40, 18)
//        popupWindow.setOnDismissListener {
//            baseActivity!!.showToast("fsdfkjfk,k")
//        }
        val vanDeliveryTV = popUpView.findViewById<AppCompatTextView>(R.id.vanDeliveryTV)
        val regularDeliveryTV = popUpView.findViewById<AppCompatTextView>(R.id.regularDeliveryTV)
        vanDeliveryTV.setOnClickListener {
            popupWindow.dismiss()
            deliveryType = 1
            binding.drivingTypeET.setText(getString(R.string.van_delivery))
        }

        regularDeliveryTV.setOnClickListener {
            popupWindow.dismiss()
            deliveryType = 2
            binding.drivingTypeET.setText(getString(R.string.regular_delivery))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == Const.CAMERA_CODE) {
            CropImage.activity(cameraUri).setAspectRatio(1, 1).start(baseActivity!!)
        } else if (resultCode == Activity.RESULT_OK && requestCode == Const.GALLERY_CODE) {
            if (data != null) {
                CropImage.activity(data.data).setAspectRatio(1, 1).start(baseActivity!!)
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == Const.DRIVING_CAMERA_CODE) {
            CropImage.activity(drivingCameraUri).setAspectRatio(1, 1).start(baseActivity!!)
        } else if (resultCode == Activity.RESULT_OK && requestCode == Const.DRIVING_GALLERY_CODE) {
            if (data != null) {
                CropImage.activity(data.data).setAspectRatio(1, 1).start(baseActivity!!)
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val resultUri = result.uri
                if (typeSelected == Const.IMAGE_PERMISSION) {
                    cameraPath = resultUri.path
                    Glide.with(baseActivity!!).load(resultUri.path).into(binding.profileCIV)
                } else if (typeSelected == Const.DRIVING_PERMISSION) {
                    drivingCameraPath = resultUri.path
                    Glide.with(baseActivity!!).load(resultUri.path).into(binding.drivingIV)
                    binding.uploadPanDocumentTV.visibility = View.GONE
                }
//                val picture_path = mImageUtility.compressImage(resultUri.path!!)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

}
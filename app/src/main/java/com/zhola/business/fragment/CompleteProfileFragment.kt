package com.zhola.business.fragment

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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.zhola.R
import com.zhola.common.activity.BaseActivity
import com.zhola.common.activity.LoginSignUpActivity
import com.zhola.common.extensions.replaceFragWithArgs
import com.zhola.common.fragment.BaseFragment
import com.zhola.common.fragment.OtpFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.common.utils.Const
import com.zhola.common.utils.ImageUtils
import com.zhola.common.utils.OnUpgradeDialogClick
import com.zhola.databinding.FragmentCompleteBusinessProfileBinding


class CompleteProfileFragment : BaseFragment(), ClickHandler, OnUpgradeDialogClick,
    BaseActivity.PermCallback {

    lateinit var binding: FragmentCompleteBusinessProfileBinding
    var counter = 0
    private var canDeliverSelectedType = -1
    private var merchandiseSelectedType = -1
    private var managementSelectedType = -1
    private var cameraUri: Uri? = null
    private var gstCameraUri: Uri? = null
    private var panCameraUri: Uri? = null
    private var cameraPath: String? = null
    private var gstPath: String? = null
    private var panPath: String? = null
    private var typeSelected = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_complete_business_profile,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as LoginSignUpActivity).setToolbar("Basic Info", true)
        initUI()
    }

    private fun initUI() {
        setTabVisibility()
        setPopUpValues()
        binding.clickHandler = this
        binding.basicInfo.clickHandler = this
        binding.essential.clickHandler = this
        binding.document.clickHandler = this
    }

    private fun setPopUpValues() {
        if (canDeliverSelectedType != -1) {
            if (canDeliverSelectedType == 0) {
                binding.essential.canDeliverTV.text = getString(R.string.yes)
            } else {
                binding.essential.canDeliverTV.text = getString(R.string.no)
            }
        }

        if (merchandiseSelectedType != -1) {
            if (merchandiseSelectedType == 0) {
                binding.essential.digitalTV.text = getString(R.string.yes)
            } else {
                binding.essential.digitalTV.text = getString(R.string.no)
            }
        }

        if (managementSelectedType != -1) {
            if (canDeliverSelectedType == 0) {
                binding.essential.managementSystemTV.text = getString(R.string.yes)
            } else {
                binding.essential.managementSystemTV.text = getString(R.string.no)
            }
        }
    }

    private fun setTabVisibility() {
        if (counter == 0) {
            (baseActivity as LoginSignUpActivity).setToolbar("Basic Info", true)
            showBasicInfoTab()
        } else if (counter == 1) {
            (baseActivity as LoginSignUpActivity).setToolbar("Essential Info", true)
            showEssentialTab()
        } else {
            (baseActivity as LoginSignUpActivity).setToolbar("Document Info", true)
            showDocumentTab()
        }
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.basicInfoTV -> {
                binding.viewPager.currentItem = 0
            }

            R.id.saveBT -> {
                if (isBasicInfoValidate()) {
                    counter = 1
                    setTabVisibility()
                }
            }

            R.id.saveNextBT -> {
                if (isEssentialValidate()) {
                    counter = 2
                    setTabVisibility()
                }
            }

            R.id.saveDocBT -> {
                if (isDocumentValidate()) {
                    counter = 2
                    val bundle = Bundle()
                    bundle.putInt("roleId", Const.TYPE_VENDOR)
                    baseActivity!!.replaceFragWithArgs(OtpFragment(), R.id.login_frame, bundle)
                }
            }

            R.id.essentialTV -> {
                binding.viewPager.currentItem = 1
            }

            R.id.documentsTV -> {
                binding.viewPager.currentItem = 2
            }

            R.id.canDeliverCV -> {
                openPopupWindow(view)
            }

            R.id.merchandiseCV -> {
                openPopupWindow(view)
            }

            R.id.managementSystemCV -> {
                openPopupWindow(view)
            }

            R.id.cameraIV -> {
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

            R.id.gstDocumentCV -> {
                if (baseActivity!!.checkPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), Const.GST_PERMISSION, this
                    )
                ) {
                    typeSelected = Const.GST_PERMISSION
                    gstCameraUri = ImageUtils.getInstance()
                        .CameraGalleryIntent(
                            baseActivity!!,
                            Const.GST_CAMERA_CODE,
                            Const.GST_GALLERY_CODE
                        )
                }
            }

            R.id.panDocumentCV -> {
                if (baseActivity!!.checkPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), Const.PAN_PERMISSION, this
                    )
                ) {
                    typeSelected = Const.PAN_PERMISSION
                    panCameraUri = ImageUtils.getInstance()
                        .CameraGalleryIntent(
                            baseActivity!!,
                            Const.PAN_CAMERA_CODE,
                            Const.PAN_GALLERY_CODE
                        )
                }
            }
        }
    }

    private fun showDocumentTab() {
        reset()
        binding.basicInfo.root.visibility = View.GONE
        binding.essential.root.visibility = View.GONE
        binding.document.root.visibility = View.VISIBLE

        binding.documentsTV.background =
            ContextCompat.getDrawable(baseActivity!!, R.drawable.orange_background)
        binding.documentsTV.setTextColor(
            ContextCompat.getColor(
                baseActivity!!,
                R.color.white
            )
        )
    }

    private fun showEssentialTab() {
        reset()
        binding.basicInfo.root.visibility = View.GONE
        binding.essential.root.visibility = View.VISIBLE
        binding.document.root.visibility = View.GONE
        binding.essentialTV.background =
            ContextCompat.getDrawable(baseActivity!!, R.drawable.orange_background)
        binding.essentialTV.setTextColor(
            ContextCompat.getColor(
                baseActivity!!,
                R.color.white
            )
        )
    }

    private fun showBasicInfoTab() {
        reset()
        binding.basicInfo.root.visibility = View.VISIBLE
        binding.essential.root.visibility = View.GONE
        binding.document.root.visibility = View.GONE
        binding.basicInfoTV.background =
            ContextCompat.getDrawable(baseActivity!!, R.drawable.orange_background)
        binding.basicInfoTV.setTextColor(
            ContextCompat.getColor(
                baseActivity!!,
                R.color.white
            )
        )
    }

    private fun isBasicInfoValidate(): Boolean {
        if (cameraPath.isNullOrEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_upload_the_business_logo),
                this
            )
        } else if (binding.basicInfo.businessNameET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_business_name), this)
        } else if (binding.basicInfo.phoneNumberET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_business_phone_number),
                this
            )
        } else if (binding.basicInfo.emailET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_business_email_address),
                this
            )
        } else if (binding.basicInfo.addressOneET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_address_line_one),
                this
            )
        } else if (binding.basicInfo.addressTwoET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_address_line_two),
                this
            )
        } else if (binding.basicInfo.cityET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_city), this)
        } else if (binding.basicInfo.stateET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_state), this)
        } else if (binding.basicInfo.zipCodeET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_zip_code), this)
        } else if (binding.basicInfo.countryET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_country), this)
        } else {
            return true
        }
        return false
    }

    private fun isEssentialValidate(): Boolean {
        if (binding.essential.companyTypeET.text.toString().isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_company_type), this)
        } else if (binding.essential.businessCategoryET.text.toString().isEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_business_category),
                this
            )
        } else if (canDeliverSelectedType == -1) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_select_the_can_deliver), this)
        } else if (merchandiseSelectedType == -1) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_select_the_is_merchandise_digital),
                this
            )
        } else if (managementSelectedType == -1) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_select_the_has_inventory_management_system),
                this
            )
        } else {
            return true
        }
        return false
    }

    private fun isDocumentValidate(): Boolean {
        if (binding.document.gstNumberET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_gst_number), this)
        } else if (gstPath.isNullOrEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_upload_the_image_of_gst_number),
                this
            )
        } else if (binding.document.panNumberET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_pan_number), this)
        } else if (panPath.isNullOrEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_upload_the_image_of_pan), this)
        } else {
            return true
        }
        return false
    }

    private fun openPopupWindow(buttonView: View) {
        val popUpView: View = layoutInflater.inflate(
            R.layout.profile_popup,
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
        val yesTV = popUpView.findViewById<AppCompatTextView>(R.id.yesTV)
        val noTV = popUpView.findViewById<AppCompatTextView>(R.id.noTV)
        yesTV.setOnClickListener {
            popupWindow.dismiss()
            when (buttonView.id) {
                R.id.canDeliverCV -> {
                    canDeliverSelectedType = 0
                    binding.essential.canDeliverTV.text = getString(R.string.yes)
                }

                R.id.merchandiseCV -> {
                    merchandiseSelectedType = 0
                    binding.essential.digitalTV.text = getString(R.string.yes)
                }

                R.id.managementSystemCV -> {
                    managementSelectedType = 0
                    binding.essential.managementSystemTV.text = getString(R.string.yes)
                }
            }
        }

        noTV.setOnClickListener {
            popupWindow.dismiss()
            when (buttonView.id) {
                R.id.canDeliverCV -> {
                    canDeliverSelectedType = 1
                    binding.essential.canDeliverTV.text = getString(R.string.no)
                }

                R.id.merchandiseCV -> {
                    merchandiseSelectedType = 1
                    binding.essential.digitalTV.text = getString(R.string.no)
                }

                R.id.managementSystemCV -> {
                    managementSelectedType = 1
                    binding.essential.managementSystemTV.text = getString(R.string.no)
                }
            }
        }
    }

    private fun reset() {
        binding.basicInfoTV.background =
            ContextCompat.getDrawable(baseActivity!!, R.drawable.white_background)
        binding.essentialTV.background =
            ContextCompat.getDrawable(baseActivity!!, R.drawable.white_background)
        binding.documentsTV.background =
            ContextCompat.getDrawable(baseActivity!!, R.drawable.white_background)
        binding.basicInfoTV.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.black))
        binding.essentialTV.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.black))
        binding.documentsTV.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.black))
    }

    override fun onUpgradeDialogClick(action: String) {

    }

    override fun permGranted(resultCode: Int) {
        if (resultCode == Const.IMAGE_PERMISSION) {
            typeSelected = Const.IMAGE_PERMISSION
            cameraUri = ImageUtils.getInstance()
                .CameraGalleryIntent(baseActivity!!, Const.CAMERA_CODE, Const.GALLERY_CODE)
        } else if (resultCode == Const.GST_PERMISSION) {
            typeSelected = Const.GST_PERMISSION
            gstCameraUri = ImageUtils.getInstance()
                .CameraGalleryIntent(
                    baseActivity!!,
                    Const.GST_CAMERA_CODE,
                    Const.GST_GALLERY_CODE
                )
        } else if (resultCode == Const.PAN_PERMISSION) {
            typeSelected = Const.PAN_PERMISSION
            panCameraUri = ImageUtils.getInstance()
                .CameraGalleryIntent(
                    baseActivity!!,
                    Const.PAN_CAMERA_CODE,
                    Const.PAN_GALLERY_CODE
                )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == Const.CAMERA_CODE) {
            CropImage.activity(cameraUri).setAspectRatio(1, 1).start(baseActivity!!)
        } else if (resultCode == Activity.RESULT_OK && requestCode == Const.GALLERY_CODE) {
            if (data != null) {
                CropImage.activity(data.data).setAspectRatio(1, 1).start(baseActivity!!)
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == Const.GST_CAMERA_CODE) {
            CropImage.activity(gstCameraUri).setAspectRatio(1, 1).start(baseActivity!!)
        } else if (resultCode == Activity.RESULT_OK && requestCode == Const.GST_GALLERY_CODE) {
            if (data != null) {
                CropImage.activity(data.data).setAspectRatio(1, 1).start(baseActivity!!)
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == Const.PAN_CAMERA_CODE) {
            CropImage.activity(panCameraUri).setAspectRatio(1, 1).start(baseActivity!!)
        } else if (resultCode == Activity.RESULT_OK && requestCode == Const.PAN_GALLERY_CODE) {
            if (data != null) {
                CropImage.activity(data.data).setAspectRatio(1, 1).start(baseActivity!!)
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val resultUri = result.uri
                if (typeSelected == Const.IMAGE_PERMISSION) {
                    cameraPath = resultUri.path
                    Glide.with(baseActivity!!).load(resultUri.path).into(binding.basicInfo.logoIV)
                } else if (typeSelected == Const.GST_PERMISSION) {
                    gstPath = resultUri.path
                    Glide.with(baseActivity!!).load(resultUri.path).into(binding.document.gstIV)
                    binding.document.uploadDocumentTV.visibility = View.GONE
                } else if (typeSelected == Const.PAN_PERMISSION) {
                    panPath = resultUri.path
                    Glide.with(baseActivity!!).load(resultUri.path)
                        .into(binding.document.panDocumentIV)
                    binding.document.uploadPanDocumentTV.visibility = View.GONE
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    override fun permDenied(resultCode: Int) {

    }
}
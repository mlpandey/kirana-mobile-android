package com.zhola.business.fragment

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
import com.zhola.databinding.FragmentDocumentBinding

class DocumentFragment : BaseFragment(), ClickHandler, OnUpgradeDialogClick,
    BaseActivity.PermCallback {

    private lateinit var binding: FragmentDocumentBinding
    private var typeSelected = 0
    private var gstCameraUri: Uri? = null
    private var panCameraUri: Uri? = null
    private var gstPath: String? = null
    private var panPath: String? = null
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_document, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (baseActivity as LoginSignUpActivity).setToolbar("Business Profile", true)
        initUI()
    }

    private fun initUI() {
        mViewModel = ViewModelProvider(this).get(LoginSignUpViewModel::class.java)
        mViewModel.setData(baseActivity!!)
        binding.clickHandler = this
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.saveDocBT -> {
                if (isDocumentValidate()) {
                    signUpData!!.gstNumber = binding.gstNumberET.text!!.trim().toString()
                    signUpData!!.gstDocument = gstPath!!
                    signUpData!!.panNumber = binding.panNumberET.text!!.trim().toString()
                    signUpData!!.panDocument = panPath!!
                    signUpData!!.role = Const.ROLE_VENDOR
                    mViewModel.hitOtpApi(signUpData!!,false)
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

    private fun isDocumentValidate(): Boolean {
        if (binding.gstNumberET.text!!.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_gst_number), this)
        } else if (gstPath.isNullOrEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_upload_the_image_of_gst_number),
                this
            )
        } else if (binding.panNumberET.text!!.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_pan_number), this)
        } else if (panPath.isNullOrEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_upload_the_image_of_pan), this)
        } else {
            return true
        }
        return false
    }

    override fun onUpgradeDialogClick(action: String) {

    }

    override fun permGranted(resultCode: Int) {
        if (resultCode == Const.GST_PERMISSION) {
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

    override fun permDenied(resultCode: Int) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == Const.GST_CAMERA_CODE) {
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
                if (typeSelected == Const.GST_PERMISSION) {
                    gstPath = resultUri.path
                    Glide.with(baseActivity!!).load(resultUri.path).into(binding.gstIV)
                    binding.uploadDocumentTV.visibility = View.GONE
                } else if (typeSelected == Const.PAN_PERMISSION) {
                    panPath = resultUri.path
                    Glide.with(baseActivity!!).load(resultUri.path)
                        .into(binding.panDocumentIV)
                    binding.uploadPanDocumentTV.visibility = View.GONE
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.e("error", error.localizedMessage)
            }
        }
    }
}
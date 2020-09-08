package com.zhola.driver.fragment

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
import com.zhola.R
import com.zhola.common.activity.BaseActivity
import com.zhola.common.extensions.replaceFragWithArgs
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.fragment.BaseFragment
import com.zhola.common.fragment.CongratulationsFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.common.utils.Const
import com.zhola.common.utils.ImageUtils
import com.zhola.databinding.FragmentCompleteOrderBinding
import com.zhola.driver.activity.DriverMainActivity

class CompleteOrderFragment : BaseFragment(), ClickHandler, BaseActivity.PermCallback {

    private lateinit var binding: FragmentCompleteOrderBinding
    private var orderUri: Uri? = null
    private var orderPath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_complete_order, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as DriverMainActivity).setToolbar("Complete Order", true, false)
        initUI()
    }

    private fun initUI() {
        binding.clickHandler = this
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.completeOrderBT -> {
                val bundle = Bundle()
                bundle.putInt("roleId", Const.TYPE_DRIVER_ORDER)
                baseActivity!!.replaceFragWithArgs(
                    CongratulationsFragment(),
                    R.id.driver_container,
                    bundle
                )
            }

            R.id.uploadCV -> {
                if (baseActivity!!.checkPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), Const.DRIVING_PERMISSION, this
                    )
                ) {
                    orderUri = ImageUtils.getInstance()
                        .CameraGalleryIntent(
                            baseActivity!!,
                            Const.DRIVING_CAMERA_CODE,
                            Const.DRIVING_GALLERY_CODE
                        )
                }
            }
        }
    }

    override fun permGranted(resultCode: Int) {
        if (resultCode == Const.DRIVING_PERMISSION) {

        }
    }

    override fun permDenied(resultCode: Int) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == Const.DRIVING_CAMERA_CODE) {
            CropImage.activity(orderUri).setAspectRatio(1, 1).start(baseActivity!!)
        } else if (resultCode == Activity.RESULT_OK && requestCode == Const.DRIVING_GALLERY_CODE) {
            if (data != null) {
                CropImage.activity(data.data).setAspectRatio(1, 1).start(baseActivity!!)
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val resultUri = result.uri
                orderPath = resultUri.path
                Glide.with(baseActivity!!).load(resultUri.path).into(binding.drivingIV)
                binding.uploadPanDocumentTV.visibility = View.GONE
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}
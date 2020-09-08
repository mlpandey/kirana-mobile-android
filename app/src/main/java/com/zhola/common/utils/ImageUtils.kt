package com.zhola.common.utils

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import com.zhola.BuildConfig
import com.zhola.R
import java.io.File


class ImageUtils {
    val uri: Uri
        get() {
            val root = file
            root.mkdirs()
            val filename = uniqueImageFilename
            val sdImageMainDirectory = File(root, filename)

            return Uri.fromFile(sdImageMainDirectory)
        }
    val filename: String
        get() {
            val root = file
            root.mkdirs()
            val filename = uniqueImageFilename
            val file = File(root, filename)
            return file.absolutePath
        }
    fun CameraGalleryIntent(
        activity: Activity,
        cameraRequestCode: Int,
        galleryRequestCode: Int
    ): Uri {


        val root = file
        root.mkdirs()
        val filename = uniqueImageFilename
        val sdImageMainDirectory = File(root, filename)
//         val outputFileUri = Uri.fromFile(sdImageMainDirectory)
//        val outputFileUri = Uri.fromFile(sdImageMainDirectory)/*FileProvider.getUriForFile(
//            context,
//                context.packageName + ".provider", sdImageMainDirectory)*/
        val outputFileUri = FileProvider.getUriForFile(
            activity,
            BuildConfig.APPLICATION_ID.toString() + ".provider",
            sdImageMainDirectory
        )

        /*  val root = file
          root.mkdirs()
          val filename = uniqueImageFilename
          val sdImageMainDirectory = File(root, filename)
  //         val outputFileUri = Uri.fromFile(sdImageMainDirectory)
          val outputFileUri = FileProvider.getUriForFile(
              activity,
              BuildConfig.APPLICATION_ID.toString() + ".provider",
              sdImageMainDirectory
          )
  //        val outputFileUri = Uri.fromFile(sdImageMainDirectory)
          *//*FileProvider.getUriForFile(
            context,
                context.packageName + ".provider", sdImageMainDirectory)*//*
*/




        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.camera_gallery_popup)
        val lp = WindowManager.LayoutParams()
        lp.alpha = 1.0f
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.BOTTOM
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val cameraIntent = dialog.findViewById<TextView>(R.id.cameraIntent)
        val galleryIntent = dialog.findViewById<TextView>(R.id.galleryIntent)
        val cancelIntentBtn = dialog.findViewById<CardView>(R.id.cancelIntentBtn)
        cameraIntent.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            activity.startActivityForResult(intent, cameraRequestCode)
        }

        galleryIntent.setOnClickListener {
            dialog.dismiss()
            val pickIntent = Intent(Intent.ACTION_PICK)
            pickIntent.type = "image/*"
            activity.startActivityForResult(pickIntent, galleryRequestCode)
        }

        cancelIntentBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window!!.attributes = lp
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)
        if (outputFileUri != null) {
            dialog.show()
        }

        return outputFileUri

    }

    private val file: File
        get() = File(
            Environment.getExternalStorageDirectory().toString() + File.separator + "Zhola" + File.separator
        )

    companion object {

        private var instance: ImageUtils? = null

        fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int
        ): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round(height.toFloat() / reqHeight.toFloat())
                } else {
                    inSampleSize = Math.round(width.toFloat() / reqWidth.toFloat())
                }
            }
            return inSampleSize
        }

        fun getInstance(): ImageUtils {
            if (instance == null) {
                instance = ImageUtils()
            }
            return instance as ImageUtils
        }

        //return a unique file name
        val uniqueImageFilename: String
            get() = "img_" + System.currentTimeMillis() + ".png"
    }

}
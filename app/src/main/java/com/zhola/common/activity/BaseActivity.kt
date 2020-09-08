package com.zhola.common.activity

import android.Manifest
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.format.DateFormat
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.zhola.BuildConfig
import com.zhola.business.activity.BusinessMainActivity
import com.zhola.business.fragment.BasicInfoFragment
import com.zhola.business.fragment.DocumentFragment
import com.zhola.R
import com.zhola.common.utils.Const
import com.zhola.common.utils.HTTPS_RESPONSE_CODE
import com.zhola.common.utils.OnUpgradeDialogClick
import com.zhola.customer.activity.CustomerMainActivity
import com.zhola.customer.fragment.CompleteProfileFragment
import com.zhola.driver.activity.DriverMainActivity
import com.zhola.driver.fragment.CompleteOrderFragment
import io.kliks.utils.ApiResponse
import io.kliks.utils.ApiService
import io.kliks.utils.PrefStore
import io.kliks.utils.RetrofitClient
import kotlinx.android.synthetic.main.dialog_common.view.*
import kotlinx.android.synthetic.main.driver_type_layout.*
import okhttp3.Headers
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.ByteArrayOutputStream
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

open class BaseActivity : AppCompatActivity(), ApiResponse {

    var permCallback: PermCallback? = null
    private var reqCode: Int = 0
    private var progressDialog: Dialog? = null
    private var exit: Boolean = false

    //    var imageUtils: ImageUtils? = null
    var retrofitClient: RetrofitClient? = null
    var retrofit: Retrofit? = null
    var apiService: ApiService? = null
    var prefStore: PrefStore? = null
    var netWorkBroadCast: NetWorkBroadCast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressDialog()
//        imageUtils = ImageUtils.getInstance()
        retrofitClient = RetrofitClient.getInstance(this)
        retrofit = retrofitClient!!.getClient(Const.API_BASE_URL)
        apiService = retrofit!!.create(ApiService::class.java)
        prefStore = PrefStore(this)
        adjustFontScale(resources.configuration)
        initializeBroadCast()
    }

    private fun initializeBroadCast() {
        netWorkBroadCast = NetWorkBroadCast()
        registerReceiver(netWorkBroadCast!!, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun isGpsEnabled(): Boolean {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton(
                "No"
            ) { dialog, id -> dialog.cancel() }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun initFCM() {
        if (retrofitClient!!.getLoginStatus() != null) {
            val role = prefStore!!.getString("role")
            if (role == "CUSTOMER") {
                gotoCustomerMainActivity()
            } else if (role == "VENDOR") {
                gotoBusinessMainActivity()
            } else if (role == "DRIVER") {
                gotoDriverMainActivity()
            }
//        gotoCustomerMainActivity()
//        } else {
        } else {
            gotoLoginSignUpActivity()
        }
    }

//    fun saveUserData(userData: UserData) {
//        val json = Gson().toJson(userData)
//        prefStore!!.saveString("userData", json)
//    }

//    fun getUserData(): UserData {
//        val json = prefStore!!.getString("userData")
//        return Gson().fromJson(json, UserData::class.java)
//    }


    fun backAction() {
        if (exit) {
            finishAffinity()
        } else {
            showToast(getString(R.string.press_one_more_time))
            exit = true
            Handler().postDelayed({ exit = false }, (2 * 1000).toLong())
        }
    }

    fun gotoLoginSignUpActivity() {
        val intent = Intent(this, LoginSignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun gotoCustomerMainActivity() {
        val intent = Intent(this, CustomerMainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun gotoBusinessMainActivity() {
        val intent = Intent(this, BusinessMainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun gotoDriverMainActivity() {
        val intent = Intent(this, DriverMainActivity::class.java)
        startActivity(intent)
        finish()
    }

    open fun getFileToByte(filePath: String?): String? {
        var bmp: Bitmap? = null
        var bos: ByteArrayOutputStream? = null
        var bt: ByteArray? = null
        var encodeString: String? = null
        try {
            bmp = BitmapFactory.decodeFile(filePath)
            bos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bt = bos.toByteArray()
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return encodeString
    }

    fun isValidMail(email: String): Boolean {
        return email.matches("[a-zA-Z0-9._+-]+@[a-z]+\\.+[a-z]+".toRegex())
    }

    fun isValidPassword(password: String): Boolean {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[!&^%$#@()=*/.+_-])(?=\\S+$).{8,}$".toRegex())
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is AppCompatEditText || v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    Log.d("focus", "touchevent")
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    open fun adjustFontScale(configuration: Configuration) {
        Log.i("configuration.fontScale", configuration.fontScale.toString())
        configuration.fontScale = 1.0f
        val metrics: DisplayMetrics = resources.displayMetrics
        val wm =
            getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)

    }

    fun checkPermissions(
        perms: Array<String>,
        requestCode: Int,
        permCallback: PermCallback
    ): Boolean {
        this.permCallback = permCallback
        this.reqCode = requestCode
        val permsArray = ArrayList<String>()
        var hasPerms = true
        for (perm in perms) {
            if (perm == Manifest.permission.ACCESS_BACKGROUND_LOCATION) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            perm
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        permsArray.add(perm)
                        hasPerms = false
                    }
                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        this,
                        perm
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permsArray.add(perm)
                    hasPerms = false
                }
            }
        }
        if (!hasPerms) {
            val permsString = arrayOfNulls<String>(permsArray.size)
            for (i in permsArray.indices) {
                permsString[i] = permsArray[i]
            }
            ActivityCompat.requestPermissions(this@BaseActivity, permsString, Const.PERMISSION_ID)
            return false
        } else
            return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        var permGrantedBool = false
        when (requestCode) {
            Const.PERMISSION_ID -> {
                for (grantResult in grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        showToast(
                            getString(R.string.not_sufficient_permissions)
                                    + getString(R.string.app_name)
                                    + getString(R.string.permissionss)
                        )
                        permGrantedBool = false
                        break
                    } else {
                        permGrantedBool = true
                    }
                }
                if (permCallback != null) {
                    if (permGrantedBool) {
                        permCallback!!.permGranted(reqCode)
                    } else {
                        permCallback!!.permDenied(reqCode)
                    }
                }
            }
        }
    }

    interface PermCallback {
        fun permGranted(resultCode: Int)

        fun permDenied(resultCode: Int)
    }

    fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
    }

    private fun progressDialog() {
        progressDialog = Dialog(this)
        val view = View.inflate(this, R.layout.progress_dialog, null)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(view)
        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog!!.setCancelable(false)
    }

    private fun startProgressDialog() {
        if (progressDialog != null && !progressDialog!!.isShowing) {
            try {
                progressDialog!!.show()
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun stopProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            try {
                progressDialog!!.dismiss()
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }

        }
    }

    fun upgradeAlertBox(
        msg: String,
        onUpgrade: OnUpgradeDialogClick,
        action: String = ""
    ) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_common, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        mDialogView.txt_error.text = msg
        mBuilder.setCancelable(false)

        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mDialogView.txt_postive.setOnClickListener {
            mAlertDialog.dismiss()
            onUpgrade.onUpgradeDialogClick(action)
        }
    }

    override fun onProgressStart(startProgress: Boolean) {
        if (startProgress) {
            startProgressDialog()
        }
    }

    override fun onApiSuccess(
        responseCode: Int,
        responseMessage: String,
        response: String?,
        responseHeaders: Headers?
    ) {

    }

    override fun onAPiFailure(
        errorCode: Int,
        t: Throwable?,
        response: Response<String>?,
        call: Call<String>?,
        callBack: Callback<String>?
    ) {
        if (this.isFinishing) {
            return
        }

        if (errorCode == HTTPS_RESPONSE_CODE.FORBIDDEN_ERROR || errorCode == HTTPS_RESPONSE_CODE.UN_AUTHORIZATION) {
            log(getString(R.string.error), getString(R.string.session_timeout_redirecting))
            showToast(getString(R.string.session_timeout_redirecting))
            retrofitClient!!.setLoginStatus(null)
            gotoLoginSignUpActivity()
            //--------------------------------goToLogin--------------------------
        } else if (errorCode == HTTPS_RESPONSE_CODE.SERVER_ERROR) {
            log(getString(R.string.error), getString(R.string.problem_connecting_to_the_server))
            showToast(getString(R.string.problem_connecting_to_the_server))
        } else if (t != null && t is ConnectException) {
            log(getString(R.string.error), getString(R.string.request_timeout_slow_connection))
            errorSnackBar(getString(R.string.request_timeout_slow_connection), call, callBack)
        } else if (t != null && t is SocketTimeoutException) {
            log(getString(R.string.error), getString(R.string.request_timeout_slow_connection))
            errorSnackBar(getString(R.string.request_timeout_slow_connection), call, callBack)
        } else if (errorCode == HTTPS_RESPONSE_CODE.API_CRASH && t != null) {
            log(getString(R.string.error), t.message.toString())
        } else {
//            val fragment = supportFragmentManager.findFragmentById(R.id.login_frame)
//            if (fragment is LoginFragment) {
//                showToast("Your email/password combination is invalid, please click Forgot Password to reset it")
//            } else if (fragment is StartLocationFragment) {
//                showToast("Trip is not saved api error")
//            } else {
            log(
                getString(R.string.error),
                if (response != null) response.message() else if (t != null) t.message else ""
            )
            var message = getString(R.string.problem_connecting_to_the_server)
            try {
                val json = JSONObject(
                    response?.body()?.toString()
                        ?: response?.errorBody()?.string() ?: "{'message':'$message'}"
                )
                if (json.has("message")) message =
                    json.getString("message") else if (json.has("error")) message =
                    json.getString("error")
            } catch (e: java.lang.Exception) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }
            log(message)
            showToast(message)
//            }
        }
    }

    internal fun errorSnackBar(
        errorString: String,
        call: Call<String>?,
        callBack: Callback<String>?
    ) {
        val buttontext: String = if (call != null && callBack != null) {
            getString(R.string.retry_caps)
        } else {
            getString(R.string.exit_caps)
        }

        val snackbar: Snackbar = Snackbar
            .make(this.view, errorString, Snackbar.LENGTH_LONG)
            .setAction(buttontext) {
                if (call != null && callBack != null) {
                    onProgressStart(true)
                    call.clone().enqueue(callBack)
                } else {
                    finish()
                }
            }

// Changing message text color
        snackbar.setActionTextColor(Color.WHITE)
        snackbar.duration = BaseTransientBottomBar.LENGTH_LONG
// Changing action button text color

// Changing action button text color
        val sbView: View = snackbar.getView()
        val textView: TextView =
            sbView.findViewById<View>(R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.maxLines = 1
        snackbar.show()
    }

    fun log(string: String) {
        if (BuildConfig.DEBUG) {
            Log.e("BaseActivity", string)
        }
    }

    fun parseDateToddMMyyyy(inputFormat: String, outputFormat: String, time: String?): String? {
        val inputFormat = SimpleDateFormat(inputFormat)
        val outputFormat = SimpleDateFormat(outputFormat)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }
        return str
    }

    fun dateFromString(dateString: String, outputFormat: String): Date? {
        var date: Date? = null
        val format =
            SimpleDateFormat(outputFormat)
        try {
            date = format.parse(dateString)
            System.out.println(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    fun getDate(timestamp: Long, format: String): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        if (timestamp.toString().length == 10) {
            calendar.timeInMillis = timestamp * 1000L
        } else {
            calendar.timeInMillis = timestamp
        }
        val date = DateFormat.format(format, calendar).toString()
        return date
    }

    fun log(title: String, string: String?) {
        if (BuildConfig.DEBUG) {
            Log.e(title, string!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterNetworkBroadcast()
    }

    private fun unregisterNetworkBroadcast() {
        try {
            if (netWorkBroadCast != null) {
                unregisterReceiver(netWorkBroadCast)
            }
        } catch (e: IllegalArgumentException) {
            netWorkBroadCast = null
        }

    }

    override fun onProgressStop() {
        stopProgressDialog()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (this is LoginSignUpActivity) {
            val fragment = supportFragmentManager.findFragmentById(R.id.login_frame)

            if (fragment is CompleteProfileFragment) {
                fragment.onActivityResult(requestCode, resultCode, data)
            } else if (fragment is com.zhola.business.fragment.CompleteProfileFragment) {
                fragment.onActivityResult(requestCode, resultCode, data)
            } else if (fragment is com.zhola.driver.fragment.CompleteProfileFragment) {
                fragment.onActivityResult(requestCode, resultCode, data)
            } else if (fragment is BasicInfoFragment) {
                fragment.onActivityResult(requestCode, resultCode, data)
            } else if (fragment is DocumentFragment) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        } else if (this is DriverMainActivity) {
            val fragment = supportFragmentManager.findFragmentById(R.id.driver_container)
            if (fragment is CompleteOrderFragment) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    inner class NetWorkBroadCast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
//            val connectionInfo = NetworkUtil.getConnectivityStatus(context!!)
//            if (!(connectionInfo == NetworkUtil.TYPE_NOT_CONNECTED || connectionInfo == NetworkUtil.TYPE_IS_CONNECTING)) {
//                if (retrofitClient!!.getLoginStatus() != null) {
//                    uploadTripData()
//                }
//            }
        }
    }
}
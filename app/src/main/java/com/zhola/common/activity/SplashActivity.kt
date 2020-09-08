package com.zhola.common.activity

import android.os.Bundle
import android.os.Handler
import com.zhola.R

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        val handler = Handler()
        val runnable: Runnable = Runnable { initFCM() }
        handler.postDelayed(runnable, 1000)
    }
}
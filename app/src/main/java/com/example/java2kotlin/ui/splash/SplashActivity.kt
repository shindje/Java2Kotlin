package com.example.java2kotlin.ui.splash

import android.os.Handler
import com.example.java2kotlin.ui.base.BaseActivity
import com.example.java2kotlin.ui.main.MainActivity
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity<Boolean>() {
    override val model: SplashViewModel by viewModel()
    override val layoutResId: Int? = null

    override fun onResume() {
        super.onResume()
        Handler(getMainLooper()).postDelayed({model.requestUser()}, 1000L)
    }

    override fun renderData(data: Boolean) {
        data?.takeIf { it }?.let { startMainActivity() }
    }

    private fun startMainActivity() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }

}
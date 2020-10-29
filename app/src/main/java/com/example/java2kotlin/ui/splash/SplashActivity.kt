package com.example.java2kotlin.ui.splash

import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.example.java2kotlin.ui.base.BaseActivity
import com.example.java2kotlin.ui.main.MainActivity

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {
    override val viewModel: SplashViewModel by lazy { ViewModelProvider(this).get(SplashViewModel::class.java) }
    override val layoutResId: Int? = null

    override fun onResume() {
        super.onResume()
        Handler(getMainLooper()).postDelayed({viewModel.requestUser()}, 1000L)
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let { startMainActivity() }
    }

    private fun startMainActivity() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }

}
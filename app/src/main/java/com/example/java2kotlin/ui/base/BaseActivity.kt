package com.example.java2kotlin.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.java2kotlin.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseActivity<T, S: BaseViewState<T>>: AppCompatActivity() {
    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutResId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        viewModel.getViewState().observe(this, {
            if (it == null)
                return@observe
            if (it.data != null)
                renderData(it.data)
            if (it.error != null)
                renderError(it.error)
        })
    }

    private fun renderError(error: Throwable) {
        if (error.message != null)
            showError(error.message)
    }

    private fun showError(message: String?) {
        val snackbar = Snackbar.make(mainRecycler, message?:"", Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.ok, {snackbar.dismiss()})
        snackbar.show()
    }


    abstract fun renderData(data: T)
}
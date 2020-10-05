package com.example.java2kotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showToastBtn.setOnClickListener{
            val toastText = getString(R.string.hello) + ", Kotlin!"
            Toast.makeText(this, toastText, Toast.LENGTH_LONG).show()
        }
    }
}
package com.example.java2kotlin.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.java2kotlin.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var adapter: MainRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = MainRVAdapter()
        mainRecycler.adapter = adapter

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.viewState().observe(this, {value ->
            value?.let { adapter.notes = it.notes }
        })
    }
}
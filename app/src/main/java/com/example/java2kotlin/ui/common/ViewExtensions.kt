package com.example.java2kotlin.ui.common

import android.view.View

fun View.dip(value: Int): Int = context.dip(value)
fun View.dip(value: Float): Int = context.dip(value)
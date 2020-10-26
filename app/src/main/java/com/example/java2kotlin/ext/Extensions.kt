package com.example.java2kotlin.ext

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.java2kotlin.R
import com.example.java2kotlin.data.entity.Color
import java.text.SimpleDateFormat
import java.util.*

const val DATE_TIME_FORMAT = "dd.MMM.yy HH:mm"

fun Date.format(): String = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(this)
fun Color.getColorInt(context: Context): Int =
        ContextCompat.getColor(context, when(this) {
            Color.WHITE -> R.color.colorWHITE
            Color.VIOLET -> R.color.colorVIOLET
            Color.YELLOW -> R.color.colorYELLOW
            Color.RED -> R.color.colorRED
            Color.PINK -> R.color.colorPINK
            Color.GREEN -> R.color.colorGREEN
            Color.BLUE -> R.color.colorBLUE
        })
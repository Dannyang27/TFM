package com.example.tfm.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil{
    fun setTimeFromTimeStamp( timestamp: Long): String{
        return "${SimpleDateFormat("HH:mm").format(Date(timestamp))}"
    }
}
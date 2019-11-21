package com.example.tfm.notification

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.preference.PreferenceManager
import androidx.core.app.NotificationCompat
import com.example.tfm.R
import com.example.tfm.util.toRoundBitmap

object NotificationFactory {

    lateinit var sharedPref: SharedPreferences

    fun createNotification(context: Context, largeIcon: Bitmap?, userName: String,
                           text: String, priority: Int): NotificationCompat.Builder{

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val isVibrationModeOn = sharedPref.getBoolean("vibrate", false)

        val channelId = context.packageName

        val notification =  NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.es)
            .setContentTitle(userName)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(priority)
            .setAutoCancel(true)


        if(isVibrationModeOn){
            notification.setDefaults(NotificationCompat.DEFAULT_VIBRATE)
        }

        largeIcon?.let {
            notification.setLargeIcon(it.toRoundBitmap())
        }

        return notification
    }
}
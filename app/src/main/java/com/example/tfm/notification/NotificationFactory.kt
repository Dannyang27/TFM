package com.example.tfm.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import com.example.tfm.R
import com.example.tfm.activity.MainActivity
import com.example.tfm.util.toRoundBitmap

object NotificationFactory {

    fun createNotification(context: Context, largeIcon: Bitmap, title: String,
                           text: String, priority: Int): NotificationCompat.Builder{

        val channelId = context.packageName

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.es)
            .setLargeIcon(largeIcon.toRoundBitmap())
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(priority)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }
}
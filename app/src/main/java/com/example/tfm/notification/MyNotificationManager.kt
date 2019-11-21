package com.example.tfm.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.tfm.model.Message

object MyNotificationManager{

    fun createNotificationChannel(ctx: Context){
        val channelId = ctx.packageName

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = channelId
            val descriptionText = "Summary of my notification text"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun displayNotification(ctx: Context, notificationId: Int, message: Message){
        //TODO need to get info for showing message
//        val userPhoto =
//        val username =
//        val messageText =
//
//        val notification = NotificationFactory.createNotification(
//            ctx, userPhoto, username, messageText, NotificationCompat.PRIORITY_DEFAULT)
//
//        with(NotificationManagerCompat.from(ctx)){
//            notify(notificationId, notification.build())
//        }
    }
}
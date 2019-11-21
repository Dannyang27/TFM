package com.example.tfm.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tfm.activity.ChatActivity
import com.example.tfm.model.Message
import com.example.tfm.room.database.MyRoomDatabase
import com.example.tfm.util.toBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MyNotificationManager{

    fun createNotificationChannel(ctx: Context){
        val channelId = ctx.packageName

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = channelId
            val descriptionText = "Summary of my notification text"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun displayNotification(ctx: Context, notificationId: Int, message: Message){
        CoroutineScope(Dispatchers.IO).launch {
            val user = MyRoomDatabase.INSTANCE?.getUserByEmail(message.senderName)
            var userPhoto: Bitmap? = null

            user?.profilePhoto?.let{
                userPhoto = it.toBitmap()
            }

            val username = user?.name.toString()
            val messageText = message.body?.fieldOne.toString()

            val notification = NotificationFactory.createNotification(
                ctx, userPhoto, username, messageText, NotificationCompat.PRIORITY_MAX)

            notification.setContentIntent(createIntent(ctx, message.ownerId, user?.email, user?.name, user?.profilePhoto))

            with(NotificationManagerCompat.from(ctx)){
                notify(notificationId, notification.build())
            }
        }
    }

    private fun createIntent(context: Context, convId: String?, email: String?, username: String?, profilePhoto: String?): PendingIntent{
        val intent = Intent(context, ChatActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("conversationId", convId)
            putExtra("receiverEmail", email)
            putExtra("receiverName", username)
            putExtra("profilePhoto", profilePhoto)
        }

        return PendingIntent.getActivity(context, 0, intent, 0)
    }
}
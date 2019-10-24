package com.example.tfm.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.example.tfm.data.DataRepository
import org.jetbrains.anko.toast

class FirebaseListenerService : Service(){

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private val user = DataRepository.user

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        toast("Service started")

        mHandler = Handler()
        mRunnable = Runnable {
            Log.d("TFM", "User id: ${user?.id}")
            startListeningConversation()
        }

        mHandler.postDelayed(mRunnable, 1000)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        toast("Service destroyed.")
        mHandler.removeCallbacks(mRunnable)
    }

    private fun startListeningConversation() {
//        FirebaseUtil.conversationServiceListener(user?.id.toString())
        mHandler.postDelayed(mRunnable, 8000)
    }
}
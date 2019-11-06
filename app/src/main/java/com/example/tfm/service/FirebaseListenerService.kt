package com.example.tfm.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import com.example.tfm.util.FirebaseUtil
import org.jetbrains.anko.toast

class FirebaseListenerService : Service(){

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        toast("Service started")

        mHandler = Handler()
        mRunnable = Runnable {
            FirebaseUtil.startConversationListener()
            FirebaseUtil.launchUserListener()
            FirebaseUtil.launchConversationListener()
        }

        mHandler.postDelayed(mRunnable, 1000)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        toast("Service destroyed.")
        mHandler.removeCallbacks(mRunnable)
    }
}
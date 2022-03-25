package com.example.mediatorappexampleapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*

class RemoteServiceWithResponse : Service() {
    private val instance = object : IRemoteServiceWithResponse.Stub() {
        init {
            Timer().schedule(
                object : TimerTask() {
                    override fun run() {
                        Log.i("TM","TM`about to call somethingWasUpdated from service app")
                        somethingWasUpdated()
                        Log.i("TM","TM`finished calling somethingWasUpdated from service app")
                    }
                },
                5000
            )
        }

        override fun somethingWasUpdated() {
            // do nothing
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return instance
    }
}
package com.example.mediatorappexampleapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlin.math.sqrt

class RemoteService : Service() {
    val instance = object : IRemoteService.Stub() {
        override fun squareRoot(number: Double): Double {
            return sqrt(number)
        }
    }
    override fun onBind(intent: Intent): IBinder {
        return instance
    }
}
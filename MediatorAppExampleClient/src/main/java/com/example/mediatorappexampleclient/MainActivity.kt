package com.example.mediatorappexampleclient

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.example.mediatorappexampleapp.IRemoteService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var remoteService: IRemoteService?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupClickListeners()
        // # Bind to RemoteService
        Intent()
            .also { it.component = ComponentName("com.example.mediatorappexampleapp", "com.example.mediatorappexampleapp.RemoteService") }
            .also { bindService(it, serviceConnection, BIND_AUTO_CREATE) }
    }

    override fun onDestroy() {
        super.onDestroy()
        // # Unbind from RemoteService
        unbindService(serviceConnection)
    }

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            remoteService = IRemoteService.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            remoteService = null
        }
    }

    private fun setupClickListeners() {
        btn_multiply_10.setOnClickListener {
            val oldNumber =
                try {
                    edittext_number.text.toString().toDouble()
                } catch (e: NumberFormatException) {
                    toast("Invalid number")
                    return@setOnClickListener
                }
            val remoteServiceRedefined =
                try {
                    remoteService!!
                } catch (e: NullPointerException) {
                    toast("Remote Service is not yet bound. Try installing the service application.")
                    return@setOnClickListener
                }
            // # Use RemoteService
            val newNumber = remoteServiceRedefined.squareRoot(oldNumber)
            edittext_number.setText(newNumber.toString())
        }
    }
}

package com.example.mediatorappexampleclient

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mediatorappexampleapp.IRemoteService
import com.example.mediatorappexampleapp.IRemoteServiceWithResponse
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var remoteService: IRemoteService? = null
    var remoteService2: IRemoteServiceWithResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("TM","TM`MainActivity.onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // # Bind to RemoteService
        Intent()
            .also { it.component = ComponentName("com.example.mediatorappexampleapp", "com.example.mediatorappexampleapp.RemoteService") }
            .also { bindService(it, remoteServiceConnection, BIND_AUTO_CREATE) }
        // # Bind to RemoteServiceWithResponse
        Intent()
            .also { it.component = ComponentName("com.example.mediatorappexampleapp", "com.example.mediatorappexampleapp.RemoteServiceWithResponse") }
            .also { bindService(it, remoteServiceWithResponseConnection, BIND_AUTO_CREATE) }
        // # View Setup
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

    override fun onDestroy() {
        super.onDestroy()
        // # Unbind from RemoteService
        unbindService(remoteServiceConnection)
    }

    private val remoteServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            remoteService = IRemoteService.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            remoteService = null
        }
    }

    private val remoteServiceWithResponseConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            Log.i("TM", "TM`remoteServiceWithResponseConnection.onServiceConnected")
            remoteService2 = object : IRemoteServiceWithResponse.Stub() {
                override fun somethingWasUpdated() {
                    Log.i("TM", "TM`Something was updated!")
                    tv_response.text = "Something was updated!"
                }
            }
        }

        override fun onServiceDisconnected(className: ComponentName) {
            remoteService2 = null
        }
    }
}

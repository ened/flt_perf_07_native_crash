package com.example.flt_perf_07_native_crash

import android.os.Bundle
import android.util.Log
import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import java.util.logging.StreamHandler

class MainActivity : FlutterActivity() {
    private lateinit var channel: MethodChannel
    private lateinit var events: EventChannel

    private var sink: EventChannel.EventSink? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GeneratedPluginRegistrant.registerWith(this)

        events = EventChannel(flutterView, "app/events")
        events.setStreamHandler(object : StreamHandler(), EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                sink = events
            }

            override fun onCancel(arguments: Any?) {
                sink = null
            }

        })

        channel = MethodChannel(flutterView, "app")
        channel.setMethodCallHandler { call, result ->
            Log.d(TAG, "calling method: ${call.method}")

            sink?.success(1)

            // Intentionally not returning a value, so that the future will hang forever.
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

package com.example.battery

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GeneratedPluginRegistrant.registerWith(FlutterEngine(this));
        var methodChannel = MethodChannel(flutterEngine?.dartExecutor?.binaryMessenger, "battery")
        methodChannel.setMethodCallHandler { call, result ->
                    if (call.method == "getBatteryLevel"){
                        val level = getBatteryLevel()
                        if (level != -1){
                            result.success(level)
                        }else{
                            result.error("Unavailable","Battery Level is not available",null)
                        }
                    }
                }
    }

    private fun getBatteryLevel() : Int {
        val batteryLevel:Int
        batteryLevel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val manager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        }else{
            val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL,-1)*100 / intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1)
        }
        return batteryLevel;
    }
}

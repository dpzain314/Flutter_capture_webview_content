package com.example.recap_weview

import android.content.Intent
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val CHANNEL = "recapMethodChannel"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if(call.method.equals("recap")){
                val recapUrl: String = call.arguments.toString()
                val intent = Intent(this,RecapActivity::class.java)
                intent.putExtra("recapURL", recapUrl);
                startActivity(intent)
            }
        }
    }
}

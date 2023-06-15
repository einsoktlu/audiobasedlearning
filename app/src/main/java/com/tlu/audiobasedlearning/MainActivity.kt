package com.tlu.audiobasedlearning

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.alan.alansdk.AlanCallback
import com.alan.alansdk.AlanConfig
import com.alan.alansdk.button.AlanButton
import com.alan.alansdk.events.EventCommand

class MainActivity : AppCompatActivity() {
    companion object {
        // This constant is needed to verify the audio permission result
        private const val ASR_PERMISSION_REQUEST_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiKey = applicationInfo.metaData.getString("ALAN_API_KEY")
        val config = AlanConfig.builder().setProjectId(apiKey).build()
        val alanButton = findViewById<AlanButton>(R.id.alan_button)
        alanButton?.initWithConfig(config)

        val alanCallback: AlanCallback = object : AlanCallback() {
            override fun onCommand(eventCommand: EventCommand) {
                AlanAI.handleCommands(this@MainActivity, eventCommand.data.getJSONObject("data"))
            }
        }

        alanButton?.registerCallback(alanCallback)

        verifyAudioPermissions()

        findViewById<Button>(R.id.button2).setOnClickListener {
            val intent = Intent(applicationContext, MediaPlayerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun verifyAudioPermissions() {
        if (checkCallingOrSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                ASR_PERMISSION_REQUEST_CODE
            )
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == ASR_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // audio permission granted
                Toast.makeText(this, "You can now use voice commands!", Toast.LENGTH_LONG).show()
            } else {
                // audio permission denied
                Toast.makeText(this, "Please provide microphone permission to use voice.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
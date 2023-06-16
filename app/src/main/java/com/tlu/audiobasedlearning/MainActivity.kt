package com.tlu.audiobasedlearning

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.alan.alansdk.AlanConfig
import com.alan.alansdk.button.AlanButton
import com.tlu.audiobasedlearning.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        // This constant is needed to verify the audio permission result
        private const val ASR_PERMISSION_REQUEST_CODE = 0
    }

    private var alanButton: AlanButton? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val config = AlanConfig.builder().setProjectId(getString(R.string.alan_api_key)).build()
        alanButton = findViewById(R.id.alan_button)
        alanButton?.initWithConfig(config)

        AlanAI.registerCallback(this, alanButton)
        AlanAI.setVisualState(alanButton, "home")

        verifyAudioPermissions()
    }

    override fun onRestart() {
        super.onRestart()
        AlanAI.registerCallback(this, alanButton)
        AlanAI.setVisualState(alanButton, "home")
    }

    override fun onStop() {
        super.onStop()
        alanButton?.clearCallbacks()
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
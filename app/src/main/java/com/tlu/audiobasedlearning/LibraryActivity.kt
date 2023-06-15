package com.tlu.audiobasedlearning

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alan.alansdk.button.AlanButton

class LibraryActivity : AppCompatActivity() {
    private var alanButton: AlanButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        alanButton = findViewById(R.id.alan_button)
        AlanAI.registerCallback(this, alanButton)
    }

    override fun onRestart() {
        super.onRestart()
        AlanAI.registerCallback(this, findViewById(R.id.alan_button))
    }

    override fun onStop() {
        super.onStop()
        alanButton?.clearCallbacks()
    }
}
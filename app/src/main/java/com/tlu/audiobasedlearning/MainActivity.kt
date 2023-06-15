package com.tlu.audiobasedlearning

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageItemInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.ApplicationInfoFlags
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.alan.alansdk.AlanCallback
import com.alan.alansdk.AlanConfig
import com.alan.alansdk.button.AlanButton
import com.alan.alansdk.events.EventCommand
import com.budiyev.android.codescanner.BuildConfig
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    companion object {
        // This constant is needed to verify the audio permission result
        private const val ASR_PERMISSION_REQUEST_CODE = 0
    }

    private fun setListeners() {
        findViewById<ImageView>(R.id.microphone).setOnClickListener {
            if (mIsListening) {
                handleSpeechEnd()
            } else {
                handleSpeechBegin()
            }
        }
    }

    private fun setContentViewWithListeners(@LayoutRes id: Int) {
        setContentView(id)

        mUserInfoText = findViewById(R.id.user_info_text)
        mUserUtteranceOutput = findViewById(R.id.user_utterance)

        setListeners()
    }

    private var mSpeechRecognizer: SpeechRecognizer? = null
    private var mIsListening = false // this will be needed later
    private var mUserInfoText: TextView? = null
    private var mUserUtteranceOutput: TextView? = null
    private var mCommandsList: MutableList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mUserInfoText = findViewById(R.id.user_info_text)
        mUserUtteranceOutput = findViewById(R.id.user_utterance)

        val apiKey = applicationInfo.metaData.getString("ALAN_API_KEY")
        val config = AlanConfig.builder().setProjectId(apiKey).build()
        val alanButton = findViewById<AlanButton>(R.id.alan_button)
        alanButton?.initWithConfig(config)

        val alanCallback: AlanCallback = object : AlanCallback() {
            /// Handling commands from Alan AI Studio
            override fun onCommand(eventCommand: EventCommand) {
                try {
                    val command = eventCommand.data
                    val commandName = command.getJSONObject("data").getString("command")
                    // Log.d("AlanButton", "onCommand: commandName: $commandName")
                    mUserUtteranceOutput!!.text = commandName
                } catch (e: JSONException) {
                    e.message?.let { Log.e("AlanButton", it) }
                }
            }
        }

        alanButton?.registerCallback(alanCallback)

        initCommands()
        verifyAudioPermissions()
        createSpeechRecognizer()

        setListeners()

        findViewById<Button>(R.id.button2).setOnClickListener {
            val intent = Intent(applicationContext, MediaPlayerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initCommands() {
        mCommandsList = ArrayList()
        mCommandsList!!.add("library")
        mCommandsList!!.add("back")
        mCommandsList!!.add("media")
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

    private fun createSpeechRecognizer() {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        mSpeechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray) {}

            override fun onEndOfSpeech() {
                handleSpeechEnd()
            }

            override fun onError(error: Int) {
                handleSpeechEnd()
            }

            override fun onResults(results: Bundle) {
                // Called when recognition results are ready. This callback will be called when the
                // audio session has been completed and user utterance has been parsed.

                // This ArrayList contains the recognition results, if the list is non-empty,
                // handle the user utterance
                val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.size > 0) {
                    // The results are added in decreasing order of confidence to the list
                    val command = matches[0]
                    mUserUtteranceOutput!!.text = command
                    handleCommand(command)
                }
            }

            override fun onPartialResults(partialResults: Bundle) {
                // Called when partial recognition results are available, this callback will be
                // called each time a partial text result is ready while the user is speaking.
                val matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.size > 0) {
                    // handle partial speech results
                    val partialText = matches[0]
                    mUserUtteranceOutput!!.text = partialText
                }
            }

            override fun onEvent(eventType: Int, params: Bundle) {}

            private fun handleCommand(command: String) {
                // Function to handle user commands - TBD
                if (mCommandsList!!.contains(command.lowercase())) {
                    // Successful utterance, notify user
                    Toast.makeText(applicationContext, "Executing: $command", Toast.LENGTH_LONG).show()

                    when (command.lowercase()) {
                        "library" -> {
                            setContentViewWithListeners(R.layout.activity_library)
                        }

                        "back" -> {
                            setContentViewWithListeners(R.layout.activity_main)
                        }

                        "media" -> {
                            val intent = Intent(applicationContext, MediaPlayerActivity::class.java)
                            startActivity(intent)
                        }
                    }
                } else {
                    // Unsucessful utterance, show failure message on screen
                    Toast.makeText(applicationContext, "Could not recognize command", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun createIntent(): Intent {
        val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        i.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        return i
    }

    private fun handleSpeechBegin() {
        // start audio session
        mUserInfoText!!.setText(R.string.listening)
        mIsListening = true
        mSpeechRecognizer!!.startListening(createIntent())
    }

    private fun handleSpeechEnd() {
        // end audio session
        mUserInfoText!!.setText(R.string.detected_speech)
        mIsListening = false
        mSpeechRecognizer!!.cancel()
    }
}
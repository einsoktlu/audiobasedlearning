package com.tlu.audiobasedlearning

import android.content.Intent
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.alan.alansdk.AlanCallback
import com.alan.alansdk.AlanConfig
import com.alan.alansdk.button.AlanButton
import com.alan.alansdk.events.EventCommand
import org.json.JSONException
import org.json.JSONObject

class AlanAI {
    companion object {
        private fun handleCommands(commandJson: JSONObject) {

            val command: String

            try {
                command = commandJson.getString("command")
            } catch (e: JSONException) {
                e.message?.let { Log.e("AlanButton", it) }
                return
            }

            when (command) {
                "navigate" -> handleNavigationCommand(commandJson.getString("screen"))
                "play", "pause" -> handleMediaCommand(command)
            }
        }

        private fun handleNavigationCommand(screen: String) {
            val mainActivity = ActivityBase.mainActivity
            val currentActivity = ActivityBase.currentActivity

            Log.d("AlanNavigation", currentActivity.toString())

            when (screen) {
                mainActivity.getString(R.string.library_screen) -> {
                    val mainAlanButton = mainActivity.findViewById<AlanButton>(R.id.alan_button)
                    setVisualState(mainAlanButton, screen)

                    val intent = Intent(currentActivity.applicationContext, LibraryActivity::class.java)
                    currentActivity.startActivity(intent)
                }

                "back" -> {
                    // visual state is handled in the MainActivity class itself
                    currentActivity.finish()
                }

                mainActivity.getString(R.string.mediaplayer_screen) -> {
                    val mainAlanButton = mainActivity.findViewById<AlanButton>(R.id.alan_button)
                    setVisualState(mainAlanButton, screen)

                    val intent = Intent(currentActivity.applicationContext, MediaPlayerActivity::class.java)
                    currentActivity.startActivity(intent)
                }
            }
        }

        private fun handleMediaCommand(command: String) {
            when (command) {
                "play" -> {
                    val button = ActivityBase.currentActivity.findViewById<Button>(R.id.playButton)
                    if (button.isEnabled) {
                        button.callOnClick()
                    }
                }

                "pause" -> {
                    val button = ActivityBase.currentActivity.findViewById<Button>(R.id.pauseButton)
                    if (button.isEnabled) {
                        button.callOnClick()
                    }
                }
            }
        }

        private fun registerCallback(alanButton: AlanButton?) {
            val alanCallback: AlanCallback = object : AlanCallback() {
                override fun onCommand(eventCommand: EventCommand) {
                    handleCommands(eventCommand.data.getJSONObject("data"))
                }
            }

            alanButton?.registerCallback(alanCallback)
        }

        fun setVisualState(alanButton: AlanButton?, screen: String) {
            val params = JSONObject()
            params.put("screen", screen)
            alanButton?.setVisualState(params.toString())
        }

        fun initButton(activity: AppCompatActivity, alanButton: AlanButton, screen: String) {
            val config = AlanConfig.builder().setProjectId(activity.getString(R.string.alan_api_key)).build()
            alanButton.initWithConfig(config)

            registerCallback(alanButton)
            setVisualState(alanButton, screen)
        }
    }
}
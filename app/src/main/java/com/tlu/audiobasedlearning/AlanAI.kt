package com.tlu.audiobasedlearning

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alan.alansdk.AlanCallback
import com.alan.alansdk.button.AlanButton
import com.alan.alansdk.events.EventCommand
import org.json.JSONException
import org.json.JSONObject

class AlanAI {
    companion object {
        fun handleCommands(activity: AppCompatActivity, alanButton: AlanButton, commandJson: JSONObject) {
            val command: String

            try {
                command = commandJson.getString("command")
            } catch (e: JSONException) {
                e.message?.let { Log.e("AlanButton", it) }
                return
            }

            when (command) {
                "navigate" -> handleNavigationCommand(activity, alanButton, commandJson.getString("screen"))
            }
        }

        private fun handleNavigationCommand(activity: AppCompatActivity, alanButton: AlanButton, screen: String) {
            Log.d("AlanNavigation", activity.toString())
            when (screen) {
                "library" -> {
                    alanButton.clearCallbacks()
                    val intent = Intent(activity.applicationContext, LibraryActivity::class.java)
                    activity.startActivity(intent)
                }

                "back" -> {
                    alanButton.clearCallbacks()
                    activity.finish()
                }

                "media" -> {
                    alanButton.clearCallbacks()
                    val intent = Intent(activity.applicationContext, MediaPlayerActivity::class.java)
                    activity.startActivity(intent)
                }
            }
        }

        fun registerCallback(activity: AppCompatActivity, alanButton: AlanButton?) {
            val alanCallback: AlanCallback = object : AlanCallback() {
                override fun onCommand(eventCommand: EventCommand) {
                    if (alanButton != null) {
                        handleCommands(activity, alanButton, eventCommand.data.getJSONObject("data"))
                    }
                }
            }

            alanButton?.registerCallback(alanCallback)
        }
    }
}
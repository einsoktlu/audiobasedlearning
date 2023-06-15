package com.tlu.audiobasedlearning

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject

class AlanAI {
    companion object {
        fun handleCommands(activity: AppCompatActivity, commandJson: JSONObject) {
            val command: String

            try {
                command = commandJson.getString("command")
            } catch (e: JSONException) {
                e.message?.let { Log.e("AlanButton", it) }
                return
            }

            when (command) {
                "navigate" -> handleNavigationCommand(activity, commandJson.getString("screen"))
            }
        }

        private fun handleNavigationCommand(activity: AppCompatActivity, screen: String) {
            when (screen) {
                "library" -> {
                    activity.setContentView(R.layout.activity_library)
                }

                "back" -> {
                    activity.setContentView(R.layout.activity_main)
                }

                "media" -> {
                    val intent = Intent(activity.applicationContext, MediaPlayerActivity::class.java)
                    activity.startActivity(intent)
                }
            }
        }
    }
}
package com.tlu.audiobasedlearning

import androidx.appcompat.app.AppCompatActivity

interface ActivityBase {
    companion object {
        lateinit var currentActivity: AppCompatActivity
        lateinit var mainActivity: AppCompatActivity
    }
}
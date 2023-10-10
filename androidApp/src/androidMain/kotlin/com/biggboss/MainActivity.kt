package com.biggboss

import MainView
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import ui.native.LinkLauncher
import ui.native.PiShared

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PiShared.setContext(this.applicationContext)
        setContent {
            MainView()
        }
    }
}
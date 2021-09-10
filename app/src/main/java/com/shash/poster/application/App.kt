package com.shash.poster.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

/**
 *@author = Shashi
 *@date = 01/08/21
 *@description = This class handles
 */
@HiltAndroidApp
class App : Application() {

    companion object{
        const val CHANNEL_1_ID = "PosterChannel7860"
        const val TAG = "App"
    }

}
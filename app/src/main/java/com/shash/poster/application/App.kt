package com.shash.poster.application

import android.app.Application
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
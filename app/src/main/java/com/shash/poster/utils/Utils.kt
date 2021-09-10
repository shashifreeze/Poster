package com.shash.poster.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shash.poster.application.App

/**
@Author: Shashi
@Date: 16-03-2021
@Description: Contains all common functions and extension functions*/

const val TAG = "UtilsTAG"

/**
 * Add divider to recyclerview
 * @param: (orientation :Int = LinearLayoutManager.VERTICAL)
 * @return: Unit
 * @author: Shashi
 * */
fun RecyclerView.addDivider() {

    if (layoutManager !is LinearLayoutManager)
        return
    addItemDecoration(
        DividerItemDecoration(
            context,
            (layoutManager as LinearLayoutManager).orientation
        )
    )
}



/**
 * Checks whether two OTP's are some or not
 */
fun ProgressBar.hide(b: Boolean) {

    if (b) {
        this.visibility = View.INVISIBLE
    } else {
        this.visibility = View.VISIBLE
    }
}

fun Context.createNotificationChannels() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel1 = NotificationChannel(
            App.CHANNEL_1_ID,
            "Poster Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel1.description = "This is used for posting messages on telegram"
        val manager = getSystemService(
            NotificationManager::class.java
        )
        try {
            manager?.createNotificationChannel(channel1)
        } catch (e: Exception) {
            Log.d(App.TAG, e.toString())
        }
    }
}












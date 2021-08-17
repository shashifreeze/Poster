package com.shash.poster.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.shash.poster.R
import com.shash.poster.application.App
import com.shash.poster.application.App.Companion.CHANNEL_1_ID
import com.shash.poster.data.Poster
import com.shash.poster.preferences.UserPreferences
import com.shash.poster.utils.extensions.copyToClipboard
import com.shash.poster.views.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 *@author = Shashi Utils.kt
 *@date = 01/08/21
 *@description = This class handles
 */
@AndroidEntryPoint
class PosterNotificationListener : NotificationListenerService() {

    private var notification: Notification? = null
    private var lastMsgTimeStamp: Long = 0

    @Inject
    lateinit var repository: ServiceRepository

    private var job: CompletableJob? = null
    private var scope: CoroutineScope? = null
    private var poster: Poster? = null

    companion object {
        const val TAG = "PosterNotification"
        const val TELEGRAM_PACKAGE_NAME = "org.telegram.messenger"
        const val URL_REGX = "https://"
    }

    override fun onCreate() {
        super.onCreate()
        job = SupervisorJob()
        scope = CoroutineScope(Dispatchers.IO + job!!)

        scope!!.launch {
            UserPreferences.getPosterData(context = applicationContext).collect {
                poster = it
            }
        }
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "NotificationListener onListenerConnected")
        createNotificationChannels()
        startForeGroundService()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                CHANNEL_1_ID,
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

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        parseNotification(sbn)
    }

    private fun parseNotification(sbn: StatusBarNotification?) {

        val packageName = sbn!!.packageName
        var copyAllowed = true
        if (packageName == TELEGRAM_PACKAGE_NAME) {
            val extras = sbn.notification.extras
            //getting title from notification
            val title = extras.getString("android.title")

            //getting text from notification
            var text: String? = extras.getCharSequence("android.text").toString()

            if (text == null) {
                if (extras["android.textLines"] != null) {
                    val charText = extras["android.textLines"] as Array<*>?
                    if (charText != null && charText.isNotEmpty()) {
                        text = charText[charText.size - 1].toString()
                    }
                }
            }

            if (title != null) {

                poster?.let {

                    if (it.copy_links_only)
                    {
                         if (text!=null && !text.contains(URL_REGX))
                         {
                             copyAllowed = false

                         }

                        Log.d(TAG, " copied Allowed$copyAllowed ")
                    }

                    if (copyAllowed && it.receiver_channel_name.contains(",")) {

                        for (name in it.receiver_channel_name.split(",")) {
                            if (title.lowercase() == name.lowercase() && sbn.notification.`when` > lastMsgTimeStamp) {
                                //copy to clipboard
                                text?.copyToClipboard(applicationContext)
                                lastMsgTimeStamp = sbn.notification.`when`
                                Log.d(
                                    TAG,
                                    "Comma copied $text , timestamp:${sbn.notification.`when`}, title=$title"
                                )
                                return@let
                            }
                        }


                    } else {

                        if (copyAllowed && title.lowercase() == it.receiver_channel_name.lowercase() && sbn.notification.`when` > lastMsgTimeStamp) {
                            //copy to clipboard
                            text?.copyToClipboard(applicationContext)
                            lastMsgTimeStamp = sbn.notification.`when`
                            Log.d(
                                TAG,
                                "copied $text , timestamp:${sbn.notification.`when`}, title=$title"
                            )

                            return@let
                        }
                    }


                }
            }
        }
    }


    private fun startForeGroundService() {
        if (notification == null) {
            val notificationIntent = Intent(applicationContext, MainActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(applicationContext, 87600, notificationIntent, 0)
            notification = NotificationCompat.Builder(applicationContext, CHANNEL_1_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setChannelId(CHANNEL_1_ID)
                .setPriority(NotificationManagerCompat.IMPORTANCE_LOW)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true) // so when data is updated don't make sound and alert in android 8.0+
                .setOngoing(true)
                .setContentText("Poster is active")
                .setVisibility(NotificationCompat.VISIBILITY_SECRET) //dont show on lock screen
                .build()
        }
        try {
            startForeground(87600, notification)
        } catch (e: Exception) {
            Log.d(TAG, "foreground not started.${e.message}")
        }
    }
}
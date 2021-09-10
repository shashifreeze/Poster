package com.shash.poster.services

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.shash.poster.R
import com.shash.poster.application.App.Companion.CHANNEL_1_ID
import com.shash.poster.data.Poster
import com.shash.poster.network.Resource
import com.shash.poster.preferences.UserPreferences
import com.shash.poster.utils.createNotificationChannels
import com.shash.poster.utils.extensions.copyToClipboard
import com.shash.poster.utils.extensions.extractUrls
import com.shash.poster.views.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.jsoup.Jsoup
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
        const val DESCRIPTION = "meta[name= description]"
        const val TITLE = "meta[name= title]"
        const val CONTENT = "content"
        const val affiliateId = "shashkr-21" //amazon
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
        applicationContext.createNotificationChannels()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeGroundService()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        //for testing
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

                    if (it.copy_links_only ) {
                        if (text != null && !text.contains(URL_REGX) or !(text.contains("amzn.to") or text.contains("amazon.in"))) {
                            copyAllowed = false
                        }

                        Log.d(TAG, " copied Allowed-$copyAllowed ")
                    }

                    if (copyAllowed && it.receiver_channel_name.contains(",")) {

                        for (name in it.receiver_channel_name.split(",")) {
                            if (title.lowercase().trim() == name.lowercase()
                                    .trim() && sbn.notification.`when` > lastMsgTimeStamp
                            ) {
                                //copy to clipboard
                                text?.copyToClipboard(applicationContext,it.excludeWords)
                                //convert to affiliate link
                                convertToAfLink(it,text)
                                lastMsgTimeStamp = sbn.notification.`when`
//                                Log.d(
//                                    TAG,
//                                    "Comma copied $text , timestamp:${sbn.notification.`when`}, title=$title"
//                                )
                                return
                            }
                        }


                    } else {

                        if (copyAllowed && title.lowercase()
                                .trim() == it.receiver_channel_name.lowercase()
                                .trim() && sbn.notification.`when` > lastMsgTimeStamp
                        ) {
                            //copy to clipboard
                            text?.copyToClipboard(applicationContext,it.excludeWords)
                            //convert to affiliate link
                            convertToAfLink(it,text)
                            lastMsgTimeStamp = sbn.notification.`when`
//                            Log.d(
//                                TAG,
//                                "copied $text , timestamp:${sbn.notification.`when`}, title=$title"
//                            )

                            return
                        }
                    }
                }
            }
        }
    }

    private fun convertToAfLink(poster:Poster,text: String?) {

        //If in the setting convert affiliate is false
        if (!poster.convert_affiliate){
            return
        }
        // if text is null
        if (text == null) {
            return
        }

        var exclude = false

        for(word in poster.excludeWords.split(",")){
            if (text.toString().lowercase().contains(word.lowercase()))
            {
                exclude = true
                return
            }
        }

        //Return if excluded word found in content
        if (exclude) return


        Thread {

            val urls: List<String> = text.extractUrls()
            var replacedText:String = text

            for (u in urls) {
                try {
                    var originalUrl = Jsoup.connect(u).execute().url().toString()

                    val baseUrl = originalUrl.split("tag=")[0]
                    originalUrl = baseUrl+"tag="+affiliateId

                   replacedText =  replacedText.replace(u, originalUrl, ignoreCase = true)
                    Log.d(TAG,"OriginalUrl=$originalUrl , Old Url = $u")
                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                }
            }
            Log.d(TAG, replacedText)
            postMessage(replacedText, "postTitle")

        }.start()
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


    private fun postMessage(originalUrl: String, postTitle: String) {
        scope!!.launch {

            val v = repository.postMessage(
                chatId = poster!!.receiver_channel_chat_id,
                message = originalUrl
            )
            when (v) {
                is Resource.Failure -> {
                    Log.d(TAG, "Failed-${v.message}")
                }
                Resource.Loading -> TODO()
                is Resource.Success -> Log.d(TAG, "Success")
            }

        }
    }


}
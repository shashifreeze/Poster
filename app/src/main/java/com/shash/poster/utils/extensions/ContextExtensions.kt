package com.shash.poster.utils.extensions

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.shash.poster.R


/**
 *@author = Shashi
 *@date = 25/07/21
 *@description = This File contains Context extension functions
 */

/**
 * Shows alert dialog
 */
fun Context.showAlertDialog(
    title: String? = null,
    message: String,
    posBtnText:String?=null,
    negBtnText:String?=null,
    showNegBtn:Boolean=true,
    callback: () -> Unit
) {
    AlertDialog.Builder(this).also {
        it.setTitle(title ?: getString(R.string.alert))
        it.setMessage(message)
        it.setPositiveButton(posBtnText?:getString(R.string.yes)) { _, _ ->
            callback()
        }
        if (showNegBtn) {
            it.setNegativeButton(negBtnText ?: getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
        }

    }.create().show()
}

/**shows toast to the context
 * @param: text: String
 * @return: Unit
 * @author: Shashi
 */

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.notificationPermissionEnabled(): Boolean {
    val pkgName = packageName
    val flat = Settings.Secure.getString(
        contentResolver,
        "enabled_notification_listeners"
    )
    if (!TextUtils.isEmpty(flat)) {
        val names = flat.split(":").toTypedArray()
        for (name in names) {
            val cn = ComponentName.unflattenFromString(name)
            if (cn != null) {
                if (TextUtils.equals(pkgName, cn.packageName)) {
                    return true
                }
            }
        }
    }
    return false
}

fun  Context.openNotificationPermissionSettings()
{
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        } else {
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
        }
    } catch (e: ActivityNotFoundException) {
        showToast("Your Device Does Not Support this service.")
    }
}

/**
 * Indicates whether the specified app ins installed and can used as an intent. This
 * method checks the package manager for installed packages that can
 * respond to an intent with the specified app. If no suitable package is
 * found, this method returns false.
 *
 * @param context The application's environment.
 * @param appName The name of the package you want to check
 *
 * @return True if app is installed
 */
fun isAppAvailable(context: Context, appName: String?): Boolean {
    val pm = context.packageManager
    return try {
        pm.getPackageInfo(appName!!, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}
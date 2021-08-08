package com.shash.poster.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.shash.poster.R
import com.shash.poster.network.Resource

/**
 *@author = Shashi
 *@date = 28/07/21
 *@description = This File contains Activity extension functions
 */

/**
 * @param: (activity: Class<A>, flags: Boolean = true)
 * @return: Unit
 * @author: Shashi
 * Takes activity as argument and launch the activity*/



fun <A : Activity> Activity.startNewActivity(activity: Class<A>, flags: Boolean = true) {
    Intent(this, activity).also {
        if (flags) it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(it)
    }
}

/**
 * @param: null
 * @return: Unit
 * @author: Shashi
 * shows snakbar to the views
 * hide soft keyboard forcefully
 * */
fun Activity.hideKeyboard() {
    try {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        /** Check if no view has focus*/
        val currentFocusedView = currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    } catch (e: Exception) {
        /**Some exception caught*/
    }
}

/**
 * @param: (failure: Resource.Failure? = null, retry: (() -> Unit)? = null)
 * @return: Unit
 * @author: Shashi
 * handles api error for the api calls and shows snakbar according to the found error
 */
fun Activity.handleApiError(failure: Resource.Failure? = null, retry: (() -> Unit)? = null) {
    failure?.let {
        when {
            it.isNetworkError -> {
                if (it.message != null) {
                    showToast(it.message)

                } else {
                    showToast(getString(R.string.check_net_con))
                }

            }

            else -> {
                failure.message?.let { msg ->
                    showToast(msg)
                }
            }
        }
    }
}

/**
make the screen touch disable and enable based on disable variable value
* @param: disable: Boolean
* @return: Unit
* @author: Shashi
*/
fun Activity.disableInteraction(disable: Boolean) {
    if (disable)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
    else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}

/**
 * Shows setting dialog
 * @param: null
 * @return: Unit
 * @author: Shashi
 */

fun Activity.showSettingsDialog() {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(getString(R.string.need_permission))
    builder.setMessage(getString(R.string.need_permission_msg))
    builder.setPositiveButton(getString(R.string.go_to_settings)) { dialog: DialogInterface, _: Int ->
        dialog.cancel()
        openSettings()
    }
    if (!this.isDestroyed && !this.isFinishing) builder.show()
}

/**
 * navigating user to app settings
 *@param: null
 *@return: Unit
 *@author: Shashi
 * */
private fun Activity.openSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", this.packageName, null)
    intent.data = uri
    startActivityForResult(intent, 121)
}


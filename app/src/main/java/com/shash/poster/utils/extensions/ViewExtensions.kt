package com.shash.poster.utils.extensions

import android.view.View
import android.view.ViewGroup

import com.google.android.material.snackbar.Snackbar
import com.shash.poster.R

/**
 *@author = Shashi
 *@date = 28/07/21
 *@description = This File contains View extension functions
 */
/**
 * @param: isVisible: Boolean
 * @return: Unit
 * @author: Shashi
 * Shows and hides a view based on argument isVisible
 * */

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

/**enables and disables a view based on argument isVisible
 * @param: enabled: Boolean
 * @return: Unit
 * @author: Shashi
 * */

fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

/**
 * @param: (message: String, action: (() -> Unit)? = null)
 * @return: Unit
 * @author: Shashi
 * shows snakbar to the views
 */
fun View.snackBar(
    message: String,
    action: (() -> Unit)? = null,
    length: Int = Snackbar.LENGTH_LONG
) {
    val snackbar = Snackbar.make(this, message, length)
    /**snackbar.setBackgroundTint(this.resources.getColor(R.color.primary,null))*/

    action?.let {
        snackbar.setAction(context.getString(R.string.retry)) {
            it()
        }
    }
    snackbar.show()
}

fun View.setEnabledRecursively(enabled: Boolean) {
    isEnabled = enabled
    if (this is ViewGroup)
        (0 until childCount).map(::getChildAt).forEach { it.setEnabledRecursively(enabled) }
}


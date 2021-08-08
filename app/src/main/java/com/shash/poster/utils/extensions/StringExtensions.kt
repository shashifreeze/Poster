package com.shash.poster.utils.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 *@author = Shashi
 *@date = 25/07/21
 *@description = This File String Context extension functions
 */


/**
 *
 * Extract OTP from string and populate it in this edittext
 */
fun String.extractOTPFromMessageString(): Int? {
    val otpPattern: Pattern = Pattern.compile("(|^)\\d{6}")
    val matcher: Matcher = otpPattern.matcher(this)
    if (matcher.find()) {
        return matcher.group(0)?.toIntOrNull()
    }

    return null
}

/**
 *
 * Extract OTP from string and populate it in this edittext
 */
fun String.extractPinCode(): String? {
    val otpPattern: Pattern = Pattern.compile("(|^)\\d{6}")
    val matcher: Matcher = otpPattern.matcher(this)
    if (matcher.find()) {
        return matcher.group(0)
    }
    return null
}

/**
 * Copy string to clipboard
 */
fun String.copyToClipboard(context: Context) {
    val clipBoard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("APNL", this)
    clipBoard.setPrimaryClip(clip)
}



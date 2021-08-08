package com.shash.poster.utils

import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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









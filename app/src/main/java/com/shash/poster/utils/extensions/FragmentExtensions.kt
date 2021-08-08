package com.shash.poster.utils.extensions

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.shash.poster.R
import com.shash.poster.network.Resource

const val TAG = "FragmentExtensions"
/**
 *@author = Shashi
 *@date = 28/07/21
 *@description = This File contains Fragment extension functions
 */

fun Fragment.showToast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}


/**
 * @param: (failure: Resource.Failure? = null, retry: (() -> Unit)? = null)
 * @return: Unit
 * @author: Shashi
 * handles api error for the api calls and shows snakbar according to the found error
 */
fun Fragment.handleApiError(failure: Resource.Failure? = null, retry: (() -> Unit)? = null) {
    failure?.let { it ->
        when {
            it.isNetworkError -> {
                if (it.message != null) {
                    requireView().snackBar(
                        it.message,
                        retry
                    )
                    Log.d(TAG,"Success Error:${it.message}")

                } else {
                    requireView().snackBar(
                        getString(R.string.check_net_con),
                        retry
                    )
                }

            }

            else -> {

                failure.errorCode?.let { code->

                    when(code){

                        500 -> {requireView().snackBar("Server down! Please try again later")}

                        422->{requireView().snackBar("Unable to fetch! Please try again later")}

                        else->{
                            failure.message?.let { msg ->

                                requireView().snackBar(msg)
                                Log.d(TAG,"Error:$msg")
                            }
                        }
                    }
                }

            }
        }
    }
}


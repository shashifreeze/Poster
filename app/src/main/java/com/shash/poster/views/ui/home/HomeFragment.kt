package com.shash.poster.views.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.shash.poster.R
import com.shash.poster.data.Poster
import com.shash.poster.databinding.FragmentHomeBinding
import com.shash.poster.preferences.UserPreferences
import com.shash.poster.services.PosterNotificationListener
import com.shash.poster.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var receiverChannelId:String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        //notification service permission
        startNotificationService()

        //click listeners
        clickListeners()

        //observe
        observe()

    }

    private fun startNotificationService() {

        if (!requireContext().notificationPermissionEnabled()) {
            requireContext().showAlertDialog(
                title = "Notification Permission needed",
                message = "Please allow notification permission",
                posBtnText= "Settings",
                showNegBtn = false,
                callback = {
                    requireContext().openNotificationPermissionSettings()
                })
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            UserPreferences.getPosterData(requireContext()).collect {
                showToast("Success: Poster Data")
                showData(it)
            }
        }
    }

    private fun showData(it: Poster) {
        receiverChannelId = it.receiver_channel_id
        val data =
            "Saved Data:\nSender Channel id = ${it.sender_channel_id}\nReceiver Channel Id = ${it.receiver_channel_id}\nReceiver Channel api key = ${it.receiver_channel_api_key}\nReceiver Channel Chat id = ${it.receiver_channel_chat_id}"
        binding.resultTV.text = data
    }

    private fun clickListeners() {

        binding.submitBtn.setOnClickListener {
            validateAndSave()
        }

        binding.openChannel.setOnClickListener {
            openChannel()
        }
    }

    private fun openChannel() {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse("http://t.me/$receiverChannelId")
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val appName = "org.telegram.messenger"
        try {
            if (isAppAvailable(requireContext(), appName)) i.setPackage(
                appName
            )
            startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d(PosterNotificationListener.TAG, "Error.${e.message}")
        }
    }

    private fun validateAndSave() {
        binding.apply {
//            if (senderChannelId.validateEmpty("senderChannelId")) {
//                return
//            }

            if (receiverChannelId.validateEmpty("receiverChannelId")) {
                return
            }

//            if (receiverChannelApiKey.validateEmpty("receiverChannelApiKey")) {
//                return
//            }
//            if (receiverChannelChatId.validateEmpty("receiverChannelChatId")) {
//                return
//            }

            //all validation passed
            viewModel.savePosterData(
                senderChannelId.text.toString(),
                receiverChannelId.text.toString(),
                receiverChannelApiKey.text.toString(),
                receiverChannelChatId.text.toString()
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
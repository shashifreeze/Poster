package com.shash.poster.data

/**
 *@author = Shashi
 *@date = 01/08/21
 *@description = This class handles
 */
data class Poster(
val sender_channel_id:String,
val receiver_channel_id:String,
val receiver_channel_api_key:String,
val receiver_channel_chat_id: String
)

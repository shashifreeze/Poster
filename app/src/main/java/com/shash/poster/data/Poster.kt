package com.shash.poster.data

/**
 *@author = Shashi
 *@date = 01/08/21
 *@description = This class handles
 */
data class Poster(
    val copy_links_only:Boolean,
    val receiver_channel_name:String,
    val receiver_channel_api_key:String,
    val receiver_channel_chat_id: String,
    val convert_affiliate:Boolean,
    val excludeWords:String // separated by commas
)

package com.bitlogger.onair.model

data class StreamModel(
    val __v: Int,
    val _id: String,
    val isLive: Boolean,
    val isYoutube: Boolean,
    val isVimeo: Boolean,
    val source: String,
    val streamId: Int,
    val streamName: String,
    val thumbnail: String,
    val url: String
)
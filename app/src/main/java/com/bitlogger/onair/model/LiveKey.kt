package com.bitlogger.onair.model

data class LiveKey(
    val `data`: Data?
) {
    data class Data(
        val created_at: String,
        val id: String,
        val private_key: String
    )
}
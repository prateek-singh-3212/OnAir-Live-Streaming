package com.bitlogger.onair.model

data class LiveResponseModel(
    val created_at: String?,
    val id: String?,
    val latency_mode: String?,
    val max_continuous_duration: Int?,
    val new_asset_settings: NewAssetSettings?,
    val playback_ids: List<PlaybackId?>?,
    val reconnect_window: Int?,
    val status: String?,
    val stream_key: String?,
    val test: Boolean?
) {
    data class NewAssetSettings(
        val playback_policies: List<String?>?
    )

    data class PlaybackId(
        val id: String?,
        val policy: String?
    )
}
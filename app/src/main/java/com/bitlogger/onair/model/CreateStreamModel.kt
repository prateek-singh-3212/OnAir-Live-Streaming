package com.bitlogger.onair.model

data class CreateStreamModel(
    val new_asset_settings: NewAssetSettings?,
    val playback_policy: List<String>
) {
    data class NewAssetSettings(
        val playback_policy: List<String>
    )
}
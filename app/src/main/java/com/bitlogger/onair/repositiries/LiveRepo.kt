package com.bitlogger.onair.repositiries

import com.bitlogger.onair.model.CreateStreamModel
import com.bitlogger.onair.model.LiveResponseModel
import com.bitlogger.onair.network.apiInterface.LiveInterface
import com.bitlogger.onair.network.apiInterface.LiveKey
import retrofit2.Response
import javax.inject.Inject

class LiveRepo @Inject constructor(
    val liveInterface: LiveInterface,
    val liveKey: LiveKey
) {

    suspend fun createStream(model: CreateStreamModel, auth: String): Response<LiveResponseModel> = liveInterface.createStream(model, auth)

    suspend fun getStreamData(id: String, auth: String) = liveInterface.getStreamData(id, auth)

    suspend fun getLiveKey() = liveKey.getKey("data")
}
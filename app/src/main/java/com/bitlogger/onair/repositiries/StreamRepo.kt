package com.bitlogger.onair.repositiries

import `in`.bitlogger.kikstart.network.apiInterface.APInterface
import com.bitlogger.onair.model.StreamModel
import retrofit2.Response
import javax.inject.Inject

class StreamRepo @Inject constructor(
    var apiInterface: APInterface
) {
    suspend fun getAllStreams(): Response<Array<StreamModel>> = apiInterface.getAllStreams()

    suspend fun getStream(id: Int): Response<StreamModel> = apiInterface.getStreams(id)
}
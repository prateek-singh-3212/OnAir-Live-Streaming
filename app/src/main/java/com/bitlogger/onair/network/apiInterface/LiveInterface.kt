package com.bitlogger.onair.network.apiInterface

import com.bitlogger.onair.model.CreateStreamModel
import com.bitlogger.onair.model.LiveResponseModel
import com.bitlogger.onair.model.StreamModel
import retrofit2.Response
import retrofit2.http.*

interface LiveInterface {

    @POST("/video/v1/live-streams")
    suspend fun createStream(@Body createStreamModel: CreateStreamModel, @Header("Authorization") authHeader: String): Response<LiveResponseModel>

    @GET("/video/v1/live-streams/{streamID}")
    suspend fun getStreamData(@Path("streamID") streamID: String, @Header("Authorization") authHeader: String)
}
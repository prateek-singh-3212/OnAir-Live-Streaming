package com.bitlogger.onair.network.apiInterface

import com.bitlogger.onair.model.BotQuerry
import com.bitlogger.onair.model.BotRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BotInterface {

    @POST("/queryCheck")
    suspend fun checkQuery(@Body botQuerry: BotQuerry): Response<BotRes>
}
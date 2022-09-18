package com.bitlogger.onair.network.apiInterface

import com.bitlogger.onair.model.CreateStreamModel
import com.bitlogger.onair.model.LiveKey
import com.bitlogger.onair.model.LiveResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface LiveKey {
    @POST("/system/v1/signing-keys")
    suspend fun getKey(@Query("product") data: String): Response<LiveKey>
}
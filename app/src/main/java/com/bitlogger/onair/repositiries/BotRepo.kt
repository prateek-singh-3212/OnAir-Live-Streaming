package com.bitlogger.onair.repositiries

import com.bitlogger.onair.model.BotQuerry
import com.bitlogger.onair.model.BotRes
import com.bitlogger.onair.model.CreateStreamModel
import com.bitlogger.onair.model.LiveResponseModel
import com.bitlogger.onair.network.apiInterface.BotInterface
import com.bitlogger.onair.network.apiInterface.LiveInterface
import com.bitlogger.onair.network.apiInterface.LiveKey
import retrofit2.Response
import javax.inject.Inject

class BotRepo @Inject constructor(
    val botInterface: BotInterface,
) {

    suspend fun createStream(model: BotQuerry): Response<BotRes> = botInterface.checkQuery(model)
}
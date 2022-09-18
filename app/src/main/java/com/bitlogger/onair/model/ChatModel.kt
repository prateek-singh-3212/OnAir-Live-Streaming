package com.bitlogger.onair.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class ChatModel(
    val userName: String,
    val isBot: Boolean,
    val isModerator: Boolean,
    val message: String,
    val userImgUrl: String
) {
    constructor(): this("A", true, true, "SS", "SS")
    val gson = Gson()

    //convert a data class to a map
    fun <T> serializeToMap(): Map<String, Any> {
        return convert()
    }

    //convert an object of type I to type O
    inline fun <I, reified O> I.convert(): O {
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<O>() {}.type)
    }
}
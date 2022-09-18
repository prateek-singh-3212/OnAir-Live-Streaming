package com.bitlogger.onair.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitlogger.onair.callback.CoroutineDataPassCallbacks
import com.bitlogger.onair.model.CreateStreamModel
import com.bitlogger.onair.model.LiveKey
import com.bitlogger.onair.repositiries.LiveRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigureVM @Inject constructor(
  val liveRepo: LiveRepo
): ViewModel() {

    fun createStream(model: CreateStreamModel, callbacks: CoroutineDataPassCallbacks) {
        viewModelScope.launch {
            callbacks.isDataLoading(true)
            val responseKey = liveRepo.getLiveKey()
            if (responseKey.isSuccessful) {
                val data = responseKey.body() as LiveKey
                val response = liveRepo.createStream(model, data.data!!.private_key)
                if (response.isSuccessful) {
                    callbacks.onLoadComplete(response.body())
                    callbacks.isDataLoading(false)
                }else {
                    callbacks.onLoadFailed(response.code().toString(), response.message())
                    callbacks.isDataLoading(false)
                }
            }
        }
    }
}
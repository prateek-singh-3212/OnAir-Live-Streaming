package com.bitlogger.onair.ui.viewmodel

import `in`.bitlogger.kikstart.network.apiInterface.APInterface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitlogger.onair.callback.CoroutineDataPassCallbacks
import com.bitlogger.onair.repositiries.StreamRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityVM @Inject constructor(
    val streamRepo: StreamRepo
) : ViewModel() {

    fun getAllStreams(callbacks: CoroutineDataPassCallbacks){
        viewModelScope.launch {
            callbacks.isDataLoading(true)
            val response = streamRepo.getAllStreams()
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
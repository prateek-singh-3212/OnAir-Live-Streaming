package com.bitlogger.onair.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitlogger.onair.callback.CoroutineDataPassCallbacks
import com.bitlogger.onair.model.BotQuerry
import com.bitlogger.onair.repositiries.BotRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragSharedVM @Inject constructor(
    val botRepo: BotRepo
) : ViewModel() {
    var Qid: Int = -1
    val id = MutableLiveData<Int>()
    val ytVideoURL = MutableLiveData<String>()
    val liveURL = MutableLiveData<String>()
    val vimeoURL = MutableLiveData<String>()

    fun checkAns(botQuerry: BotQuerry, callbacks: CoroutineDataPassCallbacks) {
        viewModelScope.launch {
            callbacks.isDataLoading(true)
            val response = botRepo.createStream(botQuerry)
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
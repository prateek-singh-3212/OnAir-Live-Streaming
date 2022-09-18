package com.bitlogger.onair.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragSharedVM @Inject constructor(): ViewModel() {
    val id = MutableLiveData<Int>()
    val ytVideoURL = MutableLiveData<String>()
    val liveURL = MutableLiveData<String>()
    val vimeoURL = MutableLiveData<String>()
}
package com.bitlogger.onair.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.bitlogger.onair.R
import com.bitlogger.onair.databinding.ActivityStreamingBinding
import com.bitlogger.onair.ui.viewmodel.FragSharedVM
import com.bitlogger.onair.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StreamingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStreamingBinding
    private val sharedVM: FragSharedVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreamingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra(Constants.STREAM_ID, -1)
        if (id != -1) {
            Log.d("SS", " dasda $id")
            sharedVM.id.postValue(id)
        }

        val url = intent.getStringExtra(Constants.STREAM_URL)
        if (url != null) {
            val yt = intent.getBooleanExtra(Constants.IS_YT, false)
            val vimeo = intent.getBooleanExtra(Constants.IS_VIMEO, false)
            val live = intent.getBooleanExtra(Constants.IS_LIVE, false)
            if (yt) {
                sharedVM.ytVideoURL.postValue(url)
            }
            if (vimeo) {
                sharedVM.vimeoURL.postValue(url)
            }
            if (live) {
                sharedVM.liveURL.postValue(url)
            }
        }
    }
}
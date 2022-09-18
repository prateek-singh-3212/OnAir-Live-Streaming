package com.bitlogger.onair.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.bitlogger.onair.R
import com.bitlogger.onair.callback.CoroutineDataPassCallbacks
import com.bitlogger.onair.databinding.ActivityStreamingBinding
import com.bitlogger.onair.db.FirestoreDatabase
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
                val callbacks =object : CoroutineDataPassCallbacks {
                    override fun isDataLoading(dataLoading: Boolean) {
                        Toast.makeText(this@StreamingActivity, "$dataLoading", Toast.LENGTH_SHORT)
                    }

                    override fun <T> onLoadComplete(data: T) {
                        val model = data as HashMap<String, String>
                        sharedVM.liveURL.postValue(model["public"])
                    }

                    override fun onLoadFailed(errorCode: String, errorMessage: String) {
                        Toast.makeText(this@StreamingActivity, "Unable to connect with API", Toast.LENGTH_SHORT)
                    }
                }
                FirestoreDatabase().getDataFromFirestore("STREAM/2", callbacks)
            }
        }
    }
}
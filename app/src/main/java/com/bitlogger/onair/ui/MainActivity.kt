package com.bitlogger.onair.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitlogger.onair.adapters.StreamAdapter
import com.bitlogger.onair.callback.CoroutineDataPassCallbacks
import com.bitlogger.onair.databinding.ActivityMainBinding
import com.bitlogger.onair.model.StreamModel
import com.bitlogger.onair.ui.viewmodel.MainActivityVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val mainViewModel: MainActivityVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupStreamList()

        binding.goLive.setOnClickListener {
            val intent = Intent(this, ConfigureActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupStreamList() {
        val callbacks = object : CoroutineDataPassCallbacks {
            override fun isDataLoading(dataLoading: Boolean) {
                Toast.makeText(this@MainActivity, "$dataLoading", Toast.LENGTH_SHORT).show()
            }

            override fun <T> onLoadComplete(data: T) {
                val adapters = StreamAdapter(data as Array<StreamModel>)
                binding.mainStreamRv.apply {
                    adapter = adapters
                    layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }

            override fun onLoadFailed(errorCode: String, errorMessage: String) {
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        mainViewModel.getAllStreams(callbacks)
    }
}
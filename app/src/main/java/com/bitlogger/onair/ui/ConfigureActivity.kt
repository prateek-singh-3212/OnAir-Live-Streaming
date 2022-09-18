package com.bitlogger.onair.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bitlogger.onair.callback.CoroutineDataPassCallbacks
import com.bitlogger.onair.databinding.ActivityConfigureBinding
import com.bitlogger.onair.db.FirestoreDatabase
import com.bitlogger.onair.model.CreateStreamModel
import com.bitlogger.onair.model.LiveResponseModel
import com.bitlogger.onair.model.Stream
import com.bitlogger.onair.ui.BroadcastActivity.Preset
import com.bitlogger.onair.ui.viewmodel.ConfigureVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfigureBinding
    private val configureVM: ConfigureVM by viewModels()

    private val TAG = "MuxLive"
    private var preset = Preset.hd_720p_30fps_3mbps

    // UI Element references
    private val streamKeyField: EditText? = null

    // If you're testing in a tight loop, you won't want to paste a stream key each time.
    // Instead, set a static stream key below.
    private val defaultStreamKey = ""

    // Permissions required to access the camera, microphone, and storage
    private val PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!hasPermissions(this, PERMISSIONS)) {
            Log.i(TAG, "Requesting Permissions");
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
    }

    fun changeProfile(view: View) {
        Log.i(TAG, "Changing Profile")
        when (view.getId()) {
            binding.p360p.id -> preset = Preset.sd_360p_30fps_1mbps
            binding.p540p.id -> preset = Preset.sd_540p_30fps_2mbps
            binding.p720p.id -> preset = Preset.hd_720p_30fps_3mbps
            binding.p1080p.id -> preset = Preset.hd_1080p_30fps_5mbps
        }
    }

    fun startCamera(view: View?) {
        Log.i(TAG, "Button tapped")
        if (hasPermissions(this, PERMISSIONS)) {
            val callbacks =object :CoroutineDataPassCallbacks{
                override fun isDataLoading(dataLoading: Boolean) {
                    Toast.makeText(this@ConfigureActivity, "$dataLoading", Toast.LENGTH_SHORT)
                }

                override fun <T> onLoadComplete(data: T) {
                    val model = data as HashMap<String, String>

                    Toast.makeText(
                        this@ConfigureActivity,
                        "Starting...",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@ConfigureActivity, BroadcastActivity::class.java)
                    intent.putExtra(BroadcastActivity.intentExtraStreamKey, model["stream_key"])
                    intent.putExtra(BroadcastActivity.intentExtraPreset, preset)
                    startActivity(intent)
                }

                override fun onLoadFailed(errorCode: String, errorMessage: String) {
                    Toast.makeText(this@ConfigureActivity, "Unable to connect with API", Toast.LENGTH_SHORT)
                }
            }

            FirestoreDatabase().getDataFromFirestore("STREAM/2", callbacks)
//
//            configureVM.createStream(
//                CreateStreamModel(
//                    CreateStreamModel.NewAssetSettings(listOf("public")),
//                    listOf("public")
//                ),
//                callbacks
//            )
        } else {
            Log.i(TAG, "Requesting Permissions")
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
            // It would be nice if we could immediately move to the configure activity when the
            // permissions changed, but this seems to be hard. This'll do for now.
        }
    }

    private fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }
}
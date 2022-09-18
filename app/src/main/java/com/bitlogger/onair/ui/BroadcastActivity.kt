package com.bitlogger.onair.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bitlogger.onair.databinding.ActivityBroadcastBinding
import com.pedro.encoder.input.video.CameraHelper
import com.pedro.rtmp.utils.ConnectCheckerRtmp
import com.pedro.rtplibrary.rtmp.RtmpCamera1
import com.pedro.rtplibrary.util.FpsListener

class BroadcastActivity : AppCompatActivity(), SurfaceHolder.Callback, ConnectCheckerRtmp, View.OnTouchListener {
    lateinit var binding: ActivityBroadcastBinding

    // Logging tag
    private val TAG = "MuxLive"

    // Mux's RTMP Entry point
    private val rtmpEndpoint = "rtmp://global-live.mux.com:5222/app/"


    private lateinit var rtmpCamera: RtmpCamera1
    private val liveDesired = false
    private var streamKey: String? = null
    private var preset: Preset? = null

    // Encoding presets and profiles
    enum class Preset(var bitrate: Int, var width: Int, var height: Int, var frameRate: Int) {
        hd_1080p_30fps_5mbps(5000 * 1024, 1920, 1080, 30), hd_720p_30fps_3mbps(
            3000 * 1024,
            1280,
            720,
            30
        ),
        sd_540p_30fps_2mbps(2000 * 1024, 960, 540, 30), sd_360p_30fps_1mbps(
            1000 * 1024,
            640,
            360,
            30
        );
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBroadcastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.surfaceView.holder.addCallback(this)

        binding.button.setOnClickListener {
            goLive()
        }

        // Setup the camera
        // Setup the camera
        rtmpCamera = RtmpCamera1(binding.surfaceView, this).apply {
            setReTries(1000) // Effectively retry forever
        }

        // Listen for FPS change events to update the FPS indicator
        val callback = FpsListener.Callback { fps ->
            Log.i(TAG, "FPS: $fps")
            runOnUiThread { binding.fpslabel.text = "$fps fps" }
        }
        rtmpCamera.setFpsListener(callback)

        // Set RTMP configuration from the intent that triggered this activity
        // Set RTMP configuration from the intent that triggered this activity
        val extras = intent.extras
        if (extras != null) {
            val streamKey1 = extras.getString(intentExtraStreamKey)
            val preset1 = extras.getSerializable(intentExtraPreset) as Preset?
            preset = preset1
            streamKey = streamKey1
            Log.i(TAG, "Stream Key: $streamKey")
        }

        // Keep the screen active on the Broadcast Activity

        // Keep the screen active on the Broadcast Activity
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun goLive() {
        // Configure the stream using the configured preset
        rtmpCamera.prepareVideo(
            preset!!.width,
            preset!!.height,
            preset!!.frameRate,
            preset!!.bitrate,
            2, // Fixed 2s keyframe interval
            CameraHelper.getCameraOrientation(this)
        );
        rtmpCamera.prepareAudio(
            128 * 1024, // 128kbps
            48000, // 48k
            true // Stereo
        );

        // Start the stream!
        rtmpCamera.startStream(rtmpEndpoint + streamKey);
    }

    // RTMP Checker Callbacks
    override fun onConnectionSuccessRtmp() {
        binding.button.text = "Stop Streaming!"
        Log.i(TAG, "RTMP Connection Success")
        runOnUiThread { muxToast("RTMP Connection Successful!") }
    }

    override fun onConnectionFailedRtmp( reason: String) {
        Log.w(TAG, "RTMP Connection Failure")
        runOnUiThread {
            binding.button.text = "Reconnecting... (Cancel)"
            muxToast("RTMP Connection Failure: $reason")
        }

        // Retry RTMP connection failures every 5 seconds
        rtmpCamera!!.reTry(5000, reason)
    }

    override fun onConnectionStartedRtmp(rtmpUrl: String) {
    }

    override fun onNewBitrateRtmp(bitrate: Long) {
        Log.d(TAG, "RTMP Bitrate Changed: " + bitrate / 1024)
        this@BroadcastActivity.runOnUiThread { binding.bitrateLabel.text = "${bitrate / 1024} kbps" }
    }

    override fun onDisconnectRtmp() {
        Log.i(TAG, "RTMP Disconnect")
        this@BroadcastActivity.runOnUiThread {
            binding.bitrateLabel.text = "0 kbps"
            binding.fpslabel.text = "0 fps"
            muxToast("RTMP Disconnected!")
        }
    }

    // Little wrapper to relocate and re-pad the toast a little
    private fun muxToast(message: String) {
        val t = Toast.makeText(this@BroadcastActivity, message, Toast.LENGTH_SHORT)
        t.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 5)
        t.show()
    }

    // onAuthErrorRtmp and onAuthSuccessRtmp aren't used if you're using stream key based auth
    override fun onAuthErrorRtmp() {}

    override fun onAuthSuccessRtmp() {}

    // Touch Listener Callbacks
    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
        return false
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        // Stop the preview if it's running
        rtmpCamera!!.stopPreview()

        // Re-constrain the layout a little if the rotation of the application has changed
        val rotation = windowManager.defaultDisplay.rotation
        val l = binding.surfaceView.layoutParams as ConstraintLayout.LayoutParams
        when (rotation) {
            Surface.ROTATION_90, Surface.ROTATION_270 -> l.dimensionRatio = "w,16:9"
            else -> l.dimensionRatio = "h,9:16"
        }
        binding.surfaceView.layoutParams = l

        // Re-start the preview, which will also reset the rotation of the preview
        rtmpCamera.startPreview(1920, 1080)
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
    }

    companion object {
        // Config from the other activity comes in through an intent with some extra keys
        val intentExtraStreamKey = "STREAMKEY"
        val intentExtraPreset = "PRESET"
    }
}
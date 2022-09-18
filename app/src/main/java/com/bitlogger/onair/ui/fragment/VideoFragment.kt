package com.bitlogger.onair.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bitlogger.onair.R
import com.bitlogger.onair.databinding.FragmentVideoBinding
import com.bitlogger.onair.ui.viewmodel.FragSharedVM
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.MimeTypes
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.RequestParams
import com.loopj.android.http.TextHttpResponseHandler
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject


class VideoFragment : Fragment() {
    private lateinit var binding: FragmentVideoBinding
    private val sharedVM: FragSharedVM by activityViewModels()

    private val VIMEO_ACCESS_TOKEN = "access token"
    private val VIMDEO_ID = "Your video id"

    private val playerView: PlayerView? = null
    private lateinit var player: SimpleExoPlayer

    private var exoPlayer: ExoPlayer? = null

    //Release references
    private val playWhenReady = false //If true the player auto play the media

    private val currentWindow = 0
    private val playbackPosition: Long = 0
    private var isVimeo: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video, container, false)
        binding = FragmentVideoBinding.bind(view)

        sharedVM.ytVideoURL.observe(viewLifecycleOwner) {
            binding.youtubePlayerView.visibility = View.VISIBLE
            requireActivity().lifecycle.addObserver(binding.youtubePlayerView)
            binding.youtubePlayerView.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val videoId = getId("it")
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })
        }

        sharedVM.vimeoURL.observe(viewLifecycleOwner) {
            isVimeo = true
            binding.vimeoPlayer.visibility = View.VISIBLE
            initializePlayer(it)
            player!!.playWhenReady = true
        }

        sharedVM.liveURL.observe(viewLifecycleOwner) {
            binding.livePlayer.visibility = View.VISIBLE
            initializeLive(it)
        }
        return view
    }

    private fun initializeLive(url: String) {
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        binding.livePlayer.player = exoPlayer
        binding.livePlayer.setKeepContentOnPlayerReset(true)
        var mediaItem =  MediaItem.Builder().
        setUri(url)
            .setMimeType(MimeTypes.APPLICATION_M3U8).build()

        exoPlayer?.let { exoPlayer ->
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.playWhenReady = true
//            exoPlayer.seekTo(currentItem, 20L)

            exoPlayer.prepare()
        }

    }

    private fun getId(s: String): String = s.replace("https://www.youtube.com/watch?v=", "")

    private fun getVimeoId(s: String): String = s.replace("https://vimeo.com/", "")

    private fun createMediaItem(url: String) {
        val mediaItem: MediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
    }


    private fun initializePlayer(url: String) {

        val videolink = "https://player.vimeo.com/video/" + getVimeoId(url) + "/config"
        //To play streaming media, you need an ExoPlayer object.
        //SimpleExoPlayer is a convenient, all-purpose implementation of the ExoPlayer interface.
        player = SimpleExoPlayer.Builder(requireContext()).build()
        binding.vimeoPlayer.player = player
        callVimeoAPIRequest(videolink)

        //Supply the state information you saved in releasePlayer to your player during initialization.
        player.setPlayWhenReady(playWhenReady)
        player.seekTo(currentWindow, playbackPosition)
        player.prepare()
    }


    private fun callVimeoAPIRequest(videolink: String) {
        val client = AsyncHttpClient()
        val params = RequestParams()
        //params.put("key", "value");
        //    params.put("more", "data");
        client.get(videolink, params, object : TextHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?
            ) {
                // called when response HTTP status is "200 OK"
                try {
                    val jsonObject = JSONObject(responseString!!)
                    val req = jsonObject.getJSONObject("request")
                    val files = req.getJSONObject("files")
                    val progressive = files.getJSONArray("progressive")
                    val array1 = progressive.getJSONObject(1)
                    val v_url = array1.getString("url")
                    Log.d("URLL ", v_url)
                    createMediaItem(v_url)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                throwable: Throwable?
            ) {
                Toast.makeText(context, "Unable to play video", Toast.LENGTH_SHORT)
            }
        }
        )
    }

    override fun onStop() {
        super.onStop()
        if (isVimeo) {
            player!!.stop()
        }
    }
}
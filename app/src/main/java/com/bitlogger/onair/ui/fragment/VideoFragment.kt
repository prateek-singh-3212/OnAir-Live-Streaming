package com.bitlogger.onair.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bitlogger.onair.R
import com.bitlogger.onair.databinding.FragmentVideoBinding
import com.bitlogger.onair.ui.viewmodel.FragSharedVM
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class VideoFragment : Fragment() {
    private lateinit var binding: FragmentVideoBinding
    private val sharedVM: FragSharedVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video, container, false)
        binding = FragmentVideoBinding.bind(view)

        sharedVM.ytVideoURL.observe(viewLifecycleOwner) {
            binding.youtubePlayerView.visibility = View.VISIBLE
            requireActivity().lifecycle.addObserver(binding.youtubePlayerView)
            binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val videoId = getId("it")
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })
        }

        return view
    }
    private fun getId(s: String): String = s.replace("https://www.youtube.com/watch?v=", "")
}
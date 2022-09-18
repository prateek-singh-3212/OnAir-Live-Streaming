package com.bitlogger.onair.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bitlogger.onair.R
import com.bitlogger.onair.databinding.FragmentLiveChatBinding
import com.bitlogger.onair.databinding.FragmentQueriesBinding

class QueriesFragment : Fragment() {
      private lateinit var binding: FragmentQueriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_live_chat, container, false)
        binding = FragmentQueriesBinding.bind(view)
        return view
    }
}
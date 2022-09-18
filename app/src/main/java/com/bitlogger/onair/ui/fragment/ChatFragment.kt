package com.bitlogger.onair.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitlogger.onair.R
import com.bitlogger.onair.adapters.ViewPagerAdapter
import com.bitlogger.onair.databinding.FragmentChatBinding
import com.bitlogger.onair.model.ChatModel
import com.google.android.material.tabs.TabLayoutMediator

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding

    val tabList = arrayOf(
        "Live Chat",
        "Queries",
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        binding = FragmentChatBinding.bind(view)

        val adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.chatViewPager.adapter = adapter

        TabLayoutMediator(binding.chatTabLayout, binding.chatViewPager) { tab, position ->
            tab.text = tabList[position]
        }.attach()

        return view
    }
}
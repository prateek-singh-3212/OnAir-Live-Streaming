package com.bitlogger.onair.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitlogger.onair.R
import com.bitlogger.onair.adapters.ChatAdapter
import com.bitlogger.onair.databinding.FragmentLiveChatBinding
import com.bitlogger.onair.db.FirestoreDatabase
import com.bitlogger.onair.model.ChatModel
import com.bitlogger.onair.ui.viewmodel.FragSharedVM
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LiveChatFragment : Fragment() {
    private lateinit var binding: FragmentLiveChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val sharedVM: FragSharedVM by activityViewModels()
    private var ID: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_live_chat, container, false)
        binding = FragmentLiveChatBinding.bind(view)

        sharedVM.id.observe(viewLifecycleOwner) {
            ID = it
            fetchLiveChat(it)
        }

        binding.sendChat.setOnClickListener {
            if (ID == -1) {
                Toast.makeText(context, "Wait..", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            val uri = "LIVE_$ID/NAME_${System.currentTimeMillis()}"
            Log.d("SS", uri)
            FirestoreDatabase().addDataToFirestore(
                uri,
                mapOf(
                    "userName" to (FirebaseAuth.getInstance().currentUser?.displayName ?: "Name"),
                    "isBot" to false,
                    "isModerator" to false,
                    "message" to binding.liveMessageTv.text.toString(),
                    "userImgUrl" to (FirebaseAuth.getInstance().currentUser?.photoUrl.toString())
                )
            )
        }
        return view
    }

    private fun fetchLiveChat(id: Int) {
        Log.d("SS", " hgh $ID")
        val query: Query = FirebaseFirestore.getInstance()
            .collection("LIVE_$ID")

        val options: FirestoreRecyclerOptions<ChatModel> =
            FirestoreRecyclerOptions.Builder<ChatModel>()
                .setQuery(query, ChatModel::class.java)
                .build()

        chatAdapter = ChatAdapter(options)
        binding.liveChatRv.adapter = chatAdapter
        binding.liveChatRv.layoutManager = LinearLayoutManager(context)
    }

    override fun onStart() {
        super.onStart()
        sharedVM.id.observe(viewLifecycleOwner) {
            Log.d("SS", "Listening...")
            chatAdapter.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("SS", "Disconnecting...")
        chatAdapter.stopListening()
    }
}
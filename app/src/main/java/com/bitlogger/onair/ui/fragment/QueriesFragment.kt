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
import com.bitlogger.onair.callback.CoroutineDataPassCallbacks
import com.bitlogger.onair.databinding.FragmentQueriesBinding
import com.bitlogger.onair.db.FirestoreDatabase
import com.bitlogger.onair.model.BotQuerry
import com.bitlogger.onair.model.BotRes
import com.bitlogger.onair.model.ChatModel
import com.bitlogger.onair.ui.viewmodel.FragSharedVM
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class QueriesFragment : Fragment() {
    private lateinit var binding: FragmentQueriesBinding
    private val sharedVM: FragSharedVM by activityViewModels()
    private lateinit var chatAdapter: ChatAdapter
    private var Qid: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_queries, container, false)
        binding = FragmentQueriesBinding.bind(view)

        sharedVM.id.observe(viewLifecycleOwner) {
            fetchLiveChat(it)
        }
        binding.querySendChat.setOnClickListener {
            if (Qid == -1) {
                Toast.makeText(context, "Wait..", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            val uri = "QUERY_$Qid/NAME_${System.currentTimeMillis()}"
            Log.d("SS", uri)
            val query = binding.queryMessageTv.text.toString()
            FirestoreDatabase().addDataToFirestore(
                uri,
                mapOf(
                    "userName" to (FirebaseAuth.getInstance().currentUser?.displayName ?: "Name"),
                    "isBot" to false,
                    "isModerator" to false,
                    "message" to query,
                    "userImgUrl" to (FirebaseAuth.getInstance().currentUser?.photoUrl.toString())
                )
            )
            val callback = object :CoroutineDataPassCallbacks {
                override fun isDataLoading(dataLoading: Boolean) {
                   //
                }

                override fun <T> onLoadComplete(data: T) {
                    val model = data as BotRes
                    if (!model.isans) {
                        Toast.makeText(context, "Bot did'nt found Ans", Toast.LENGTH_SHORT).show()
                        return
                    }
                    val uriBot = "QUERY_$Qid/NAME_${System.currentTimeMillis()}"
                    FirestoreDatabase().addDataToFirestore(
                        uriBot,
                        mapOf(
                            "userName" to "On Air",
                            "isBot" to true,
                            "isModerator" to false,
                            "message" to model.ans,
                            "userImgUrl" to "https://d2cbg94ubxgsnp.cloudfront.net/Pictures/2000x1125/9/9/3/512993_shutterstock_715962319converted_920340.png"
                        )
                    )
                }

                override fun onLoadFailed(errorCode: String, errorMessage: String) {
                   Toast.makeText(context, "Unable to load querry", Toast.LENGTH_SHORT).show()
                }
            }
            sharedVM.checkAns(BotQuerry(query), callback)
        }
        return view
    }

    private fun fetchLiveChat(id: Int) {
        Log.d("SS", " hgh $Qid")
        val query: Query = FirebaseFirestore.getInstance()
            .collection("QUERY_$Qid")

        val options: FirestoreRecyclerOptions<ChatModel> =
            FirestoreRecyclerOptions.Builder<ChatModel>()
                .setQuery(query, ChatModel::class.java)
                .build()

        chatAdapter = ChatAdapter(options)
        binding.queryChatRv.adapter = chatAdapter
        binding.queryChatRv.layoutManager = LinearLayoutManager(context)
    }

    override fun onStart() {
        super.onStart()
        sharedVM.id.observe(viewLifecycleOwner) {
            Log.d("SS", "Listening...")
            sharedVM.id.observe(viewLifecycleOwner) {
                chatAdapter.startListening()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("SS", "Disconnecting...")
        chatAdapter.stopListening()
    }
}
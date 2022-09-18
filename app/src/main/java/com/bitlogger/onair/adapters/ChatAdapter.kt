package com.bitlogger.onair.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitlogger.onair.R
import com.bitlogger.onair.databinding.CardChatBinding
import com.bitlogger.onair.model.ChatModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.squareup.picasso.Picasso

class ChatAdapter(options: FirestoreRecyclerOptions<ChatModel>):
    FirestoreRecyclerAdapter<ChatModel, ChatAdapter.Viewholder>(options) {

    class Viewholder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding = CardChatBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder =
        Viewholder(LayoutInflater.from(parent.context).inflate(R.layout.card_chat, parent, false))

    override fun onBindViewHolder(holder: Viewholder, position: Int, model: ChatModel) {
        holder.binding.message.text = model.message
        holder.binding.userName.text = model.userName
        if (model.isBot) {
            holder.binding.tagBot.visibility = View.VISIBLE
        }
        if (model.isModerator) {
            holder.binding.tagModerator.visibility = View.VISIBLE
        }
        Picasso.get().load(model.userImgUrl).into(holder.binding.userProfile)
    }
}
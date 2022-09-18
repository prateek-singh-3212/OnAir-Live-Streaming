package com.bitlogger.onair.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitlogger.onair.R
import com.bitlogger.onair.databinding.CardStreamBinding
import com.bitlogger.onair.model.StreamModel
import com.bitlogger.onair.ui.StreamingActivity
import com.bitlogger.onair.util.Constants
import com.squareup.picasso.Picasso

class StreamAdapter(val data: Array<StreamModel>) :
    RecyclerView.Adapter<StreamAdapter.Viewholder>() {
    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = CardStreamBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder =
        Viewholder(LayoutInflater.from(parent.context).inflate(R.layout.card_stream, parent, false))

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.binding.streamSource.text = data[position].source
        if (data[position].isLive) {
            holder.binding.streamLive.visibility = View.VISIBLE
        }
        holder.binding.streamName.text = data[position].streamName
        Picasso.get().load(data[position].thumbnail).into(holder.binding.streamThubmnail)

        holder.binding.streamCard.setOnClickListener {
            startWatch(it.context, position)
        }
    }

    private fun startWatch(context: Context, position: Int) {
        val intent = Intent(context, StreamingActivity::class.java)
        intent.putExtra(Constants.STREAM_ID, data[position].streamId)
        intent.putExtra(Constants.STREAM_URL, data[position].url)
        if (data[position].streamId == 2) {
            intent.putExtra(Constants.IS_LIVE, true)
        } else {
            intent.putExtra(Constants.IS_LIVE, false)
        }
        intent.putExtra(Constants.IS_VIMEO, data[position].isVimeo)
        intent.putExtra(Constants.IS_YT, data[position].isYoutube)
        context.startActivity(intent)
    }

    override fun getItemCount(): Int = data.size
}
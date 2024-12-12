package com.example.projectadvocata.ui.chatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectadvocata.data.chat.MessageBot
import com.example.projectadvocata.R

class ChatAdapter(private val messageBots: MutableList<MessageBot>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.messageText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutResId = if (viewType == MESSAGE_TYPE_USER) {
            R.layout.chat_item
        } else {
            R.layout.chat_item_bot
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messageBots[position]
        holder.messageText.text = message.text
    }

    override fun getItemCount(): Int {
        return messageBots.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageBots[position].isUser) MESSAGE_TYPE_USER else MESSAGE_TYPE_BOT
    }

    companion object {
        const val MESSAGE_TYPE_USER = 1
        const val MESSAGE_TYPE_BOT = 2
    }
}

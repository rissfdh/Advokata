package com.example.projectadvocata.ui.chatbot

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectadvocata.data.chat.Message
import com.example.projectadvocata.R

class ChatAdapter(private val messages: MutableList<Message>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.messageText)
    }

    // Menentukan layout mana yang akan dipakai berdasarkan jenis pesan
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutResId = if (viewType == MESSAGE_TYPE_USER) {
            R.layout.chat_item  // Pesan pengguna
        } else {
            R.layout.chat_item_bot    // Pesan bot
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.messageText.text = message.text
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    // Menentukan tipe pesan untuk memilih layout
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) MESSAGE_TYPE_USER else MESSAGE_TYPE_BOT
    }

    companion object {
        const val MESSAGE_TYPE_USER = 1
        const val MESSAGE_TYPE_BOT = 2
    }
}

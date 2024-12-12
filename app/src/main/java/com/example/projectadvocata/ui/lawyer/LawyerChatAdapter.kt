package com.example.projectadvocata.ui.lawyer


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectadvocata.R
import com.example.projectadvocata.data.chat.MessageLawyer

class LawyerChatAdapter(private val messageLawyer: MutableList<MessageLawyer>) : RecyclerView.Adapter<LawyerChatAdapter.LawyerViewHolder>() {

    class LawyerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.messageText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawyerViewHolder {
        val layoutResId = if (viewType == MESSAGE_TYPE_USER) {
            R.layout.chat_item_user
        } else {
            R.layout.chat_item_lawyer
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return LawyerViewHolder(view)
    }

    override fun onBindViewHolder(holder: LawyerViewHolder, position: Int) {
        val message = messageLawyer[position]
        holder.messageText.text = message.text
    }

    override fun getItemCount(): Int {
        return messageLawyer.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageLawyer[position].isUser) MESSAGE_TYPE_USER else MESSAGE_TYPE_LAWYER
    }

    companion object {
        const val MESSAGE_TYPE_USER = 1
        const val MESSAGE_TYPE_LAWYER = 2
    }
}

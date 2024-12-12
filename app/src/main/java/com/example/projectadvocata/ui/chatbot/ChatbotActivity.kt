package com.example.projectadvocata.ui.chatbot

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectadvocata.data.chat.BotResponseHandler
import com.example.projectadvocata.data.chat.MessageBot
import com.example.projectadvocata.databinding.ActivityChatbotBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class ChatbotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatbotBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messageBots = mutableListOf<MessageBot>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.GONE
        supportActionBar?.title = "Chatbot"

        chatAdapter = ChatAdapter(messageBots)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = chatAdapter

        binding.sendButton.setOnClickListener {
            val userMessage = binding.inputEditText.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                messageBots.add(MessageBot(userMessage, true))
                chatAdapter.notifyItemInserted(messageBots.size - 1)
                binding.inputEditText.text.clear()
                binding.progressBar.visibility = View.VISIBLE

                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000)

                    val botResponse = BotResponseHandler.getBotResponse(userMessage)

                    binding.progressBar.visibility = View.GONE

                    messageBots.add(MessageBot(botResponse, false))
                    chatAdapter.notifyItemInserted(messageBots.size - 1)

                    binding.chatRecyclerView.scrollToPosition(messageBots.size - 1)
                }
            } else {
                Toast.makeText(this, "Tulis pesan terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

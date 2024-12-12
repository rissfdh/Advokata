package com.example.projectadvocata.ui.chatbot

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectadvocata.R
import com.example.projectadvocata.data.chat.BotResponseHandler
import com.example.projectadvocata.data.chat.Message
import com.example.projectadvocata.databinding.ActivityChatbotBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class ChatbotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatbotBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.GONE

        // Set up RecyclerView
        chatAdapter = ChatAdapter(messages)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = chatAdapter

        binding.sendButton.setOnClickListener {
            val userMessage = binding.inputEditText.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                // Add user message
                messages.add(Message(userMessage, true))
                chatAdapter.notifyItemInserted(messages.size - 1)

                // Clear the input
                binding.inputEditText.text.clear()

                // Show ProgressBar
                binding.progressBar.visibility = View.VISIBLE

                // Start Coroutine to simulate bot response
                CoroutineScope(Dispatchers.Main).launch {
                    // Simulate a delay before the bot responds
                    delay(2000)

                    // Simulate bot response after a delay (asynchronous)
                    val botResponse = BotResponseHandler.getBotResponse(userMessage)

                    // Hide ProgressBar
                    binding.progressBar.visibility = View.GONE

                    // Add bot response to the message list
                    messages.add(Message(botResponse, false))
                    chatAdapter.notifyItemInserted(messages.size - 1)

                    // Scroll to the bottom
                    binding.chatRecyclerView.scrollToPosition(messages.size - 1)
                }
            } else {
                Toast.makeText(this, "Tulis pesan terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

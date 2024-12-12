package com.example.projectadvocata.ui.lawyer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectadvocata.data.chat.LawyerResponseHandler
import com.example.projectadvocata.data.chat.MessageLawyer
import com.example.projectadvocata.databinding.ActivityChatLawyerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class LawyerChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatLawyerBinding
    private lateinit var lawyerChatAdapter: LawyerChatAdapter
    private val messageLawyer = mutableListOf<MessageLawyer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatLawyerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.GONE

        lawyerChatAdapter = LawyerChatAdapter(messageLawyer)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = lawyerChatAdapter

        binding.sendButton.setOnClickListener {
            val userMessage = binding.inputEditText.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                messageLawyer.add(MessageLawyer(userMessage, true))
                lawyerChatAdapter.notifyItemInserted(messageLawyer.size - 1)
                binding.inputEditText.text.clear()
                binding.progressBar.visibility = View.VISIBLE

                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000)

                    val lawyerResponse = LawyerResponseHandler.getLawyerResponse(userMessage)

                    binding.progressBar.visibility = View.GONE

                    messageLawyer.add(MessageLawyer(lawyerResponse, false))
                    lawyerChatAdapter.notifyItemInserted(messageLawyer.size - 1)

                    binding.chatRecyclerView.scrollToPosition(messageLawyer.size - 1)
                }
            } else {
                Toast.makeText(this, "Tulis pesan terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

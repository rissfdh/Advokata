package com.example.projectadvocata.data.chat

object LawyerResponseHandler {
    fun getLawyerResponse(lawyerMessage: String): String {
        return when {
            lawyerMessage.contains("Hallo", ignoreCase = true) -> {
                "Hallo Ka bagaimana ada yang bisa saya bantu?"
            }
            lawyerMessage.contains("Iya ka saya sedang terkena kasus pembunuhan seseorang ", ignoreCase = true) -> {
                "bisa jelaskan dengan lengkap kronologi kasusnya?"
            }
            else -> {
                "Maaf, saya tidak mengerti."
            }
        }
    }
}

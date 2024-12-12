package com.example.projectadvocata.data.chat

object BotResponseHandler {
    fun getBotResponse(userMessage: String): String {
        return when {
            userMessage.contains("pasal 122 ayat (3)", ignoreCase = true) -> {
                "apa isi dari pasal 122 ayat ( 3 ) huruf 0 isi dari pasal tersebut adalah : jika pidana denda sebagaimana dimaksud pada ayat ( 1 ) tidak dibayar dalam jangka waktu yang telah ditentukan, kekayaan atau pendapatan korporasi dapat disita dan dilelang oleh jaksa untuk melunasi pidana denda yang tidak dibayar."
            }
            userMessage.contains("pasal 228 ayat (2)", ignoreCase = true) -> {
                "apa isi dari pasal 228 ayat ( 2 ) huruf 0 isi dari pasal tersebut adalah : jika setiap orang sebagaimana dimaksud pada ayat ( 1 ) melakukan perbuatan tersebut dalam menjalankan profesinya dan pada waktu itu belum lewat 2 ( dna ) tahun sejak adanya putusan pemidanaan yang telah memperoleh kekuatan hukum tetap karena melakukan tindak pidana yang sama, dapat dijatuhi pidana tambahan berupa pencabutan hak sebagaimana dimaksud dalam pasal 86 huruf f."
            }

            else -> {
                "Maaf, saya tidak mengerti."
            }
        }
    }
}


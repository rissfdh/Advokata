package com.example.projectadvocata.ui.compliment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    val name: String,
    val description: String,
    val photo: Int
) : Parcelable

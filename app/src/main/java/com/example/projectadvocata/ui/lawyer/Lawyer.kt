package com.example.projectadvocata.ui.lawyer


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lawyer(
    val name: String,
    val description: String,
    val photo: Int
) : Parcelable

@file:Suppress("unused", "RedundantSuppression")

package com.example.projectadvocata.data.remote.response

data class DetailResponse(
    val error: Boolean? = null,
    val message: String? = null,
    val event: Event? = null
)

data class Event(
    val summary: String,
    val mediaCover: String,
    val registrants: Int,
    val imageLogo: String,
    val link: String,
    val description: String,
    val ownerName: String,
    val cityName: String,
    val quota: Int,
    val name: String,
    val id: Int,
    val beginTime: String,
    val endTime: String,
    val category: String
)


@file:Suppress("unused", "RedundantSuppression")

package com.example.projectadvocata.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class EventEntity(
    @field:ColumnInfo(name = "name")
    @field:PrimaryKey
    val name: String,

    @field:ColumnInfo(name = "beginTime")
    val beginTime: String,

    @field:ColumnInfo(name = "imageLogo")
    val imageLogo: String,

    @field:ColumnInfo(name = "summary")
    val summary: String,

    @field:ColumnInfo(name = "ownerName")
    val ownerName: String,

    @field:ColumnInfo(name = "mediaCover")
    val mediaCover: String,

    @field:ColumnInfo(name = "registrants")
    val registrants: Int,

    @field:ColumnInfo(name = "link")
    val link: String,

    @field:ColumnInfo(name = "description")
    val description: String,

    @field:ColumnInfo(name = "cityName")
    val cityName: String,

    @field:ColumnInfo(name = "quota")
    val quota: Int,

    @field:ColumnInfo(name = "id")
    val id: Int,

    @field:ColumnInfo(name = "endTime")
    val endTime: String,

    @field:ColumnInfo(name = "category")
    val category: String,

    @field:ColumnInfo(name = "bookmarked")
    var isBookmarked: Boolean,

    @field:ColumnInfo(name = "status")
    var isActive: Boolean
)
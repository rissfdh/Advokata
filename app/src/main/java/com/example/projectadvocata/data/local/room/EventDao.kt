@file:Suppress("unused", "RedundantSuppression")

package com.example.projectadvocata.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.projectadvocata.data.local.entity.EventEntity

@Dao
interface EventDao {

    @Query("SELECT * FROM event where status = 1")
    fun getUpcomingEvent(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event where status = 1 OR status = 0")
    fun getEvent(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event where status = 0")
    fun getFinishedEvent(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event where bookmarked = 1")
    fun getBookmarkedEvent(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: List<EventEntity>)

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Query("SELECT * FROM event WHERE name LIKE :query AND status = 1")
    fun searchUpcomingEvents(query: String): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE name LIKE :query AND status = 0")
    fun searchFinishedEvents(query: String): LiveData<List<EventEntity>>

    @Query("DELETE FROM event WHERE bookmarked = 0")
    suspend fun deleteAll()

    @Query("DELETE FROM event WHERE status = 1")
    suspend fun deleteUpcomingEvents()

    @Query("DELETE FROM event WHERE status = 1 OR status = 0")
    suspend fun deleteEvents()

    @Query("DELETE FROM event WHERE status = 0")
    suspend fun deleteFinishedEvents()

    @Query("SELECT * FROM event WHERE id = :eventId")
    fun getEventById(eventId: String): LiveData<EventEntity>

    @Query("SELECT EXISTS(SELECT * FROM event WHERE name = :title AND bookmarked = 1)")
    suspend fun isEventBookmarked(title: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM event WHERE name = :title AND status = 1 OR status = 0)")
    suspend fun isEventActive(title: String): Boolean
}
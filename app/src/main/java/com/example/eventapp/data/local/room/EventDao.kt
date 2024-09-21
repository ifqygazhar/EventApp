package com.example.eventapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.eventapp.data.local.entity.EventEntity


@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY beginTime DESC")
    fun getEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM events where event_status = 1 ORDER BY beginTime DESC")
    fun getEventActive(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM events where event_status = 0 ORDER BY beginTime DESC")
    fun getEventNonActive(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM events where favorite = 1")
    fun getEventsFavorite(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvent(event: List<EventEntity>)

    @Update
    fun updateEvent(event: EventEntity)

    @Query("DELETE FROM events WHERE favorite = 0 AND event_status = 1")
    fun deleteUpcomingAll()

    @Query("DELETE FROM events WHERE favorite = 0 AND event_status = 0")
    fun deleteFinishedAll()

    @Query("SELECT EXISTS(SELECT * FROM events WHERE id = :eventId AND favorite = 1)")
    fun isEventFavorite(eventId: Int): Boolean


}
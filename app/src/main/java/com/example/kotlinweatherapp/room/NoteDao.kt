package com.example.kotlinweatherapp.room

import androidx.room.*

@Dao
interface NoteDao {
    @Query("SELECT note FROM NoteEntity WHERE city LIKE :city")
    fun getNoteByCity(city: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: NoteEntity)

    @Update
    fun update(entity: NoteEntity)

    @Delete
    fun delete(entity: NoteEntity)
}
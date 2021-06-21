package com.example.kotlinweatherapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    @PrimaryKey
    val city: String,
    val note: String
)

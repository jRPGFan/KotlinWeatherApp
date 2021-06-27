package com.example.kotlinweatherapp.repository

import com.example.kotlinweatherapp.room.NoteEntity

interface NoteRepository {
    fun getNoteByCity(city: String): String?
    fun saveNote(noteEntity: NoteEntity)
    fun deleteNote(noteEntity: NoteEntity)
}
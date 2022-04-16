package com.example.kotlinweatherapp.repository

import com.example.kotlinweatherapp.room.NoteDao
import com.example.kotlinweatherapp.room.NoteEntity

class NoteRepositoryImpl(private val localNoteSource: NoteDao) : NoteRepository {
    override fun getNoteByCity(city: String): String? {
        return localNoteSource.getNoteByCity(city)
    }

    override fun saveNote(noteEntity: NoteEntity) {
        return localNoteSource.insert(noteEntity)
    }

    override fun deleteNote(noteEntity: NoteEntity) {
        return localNoteSource.delete(noteEntity)
    }
}
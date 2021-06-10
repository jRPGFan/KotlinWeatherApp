package com.example.kotlinweatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.app.App.Companion.getNoteDao
import com.example.kotlinweatherapp.repository.NoteRepository
import com.example.kotlinweatherapp.repository.NoteRepositoryImpl
import com.example.kotlinweatherapp.room.NoteEntity

class NoteViewModel(
    val noteLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val noteRepository: NoteRepository = NoteRepositoryImpl(getNoteDao())
) : ViewModel() {

    fun getNoteByCity(city: String) {
        noteLiveData.value = AppState.Loading
        noteLiveData.value = AppState.NoteLoaded(noteRepository.getNoteByCity(city))
    }

    fun saveNoteToDB(note: NoteEntity) {
        noteRepository.saveNote(note)
    }

    fun deleteNoteFromDB(note: NoteEntity) {
        noteRepository.deleteNote(note)
    }
}
package com.example.kotlinweatherapp.viewmodel

import com.example.kotlinweatherapp.model.Weather
import com.example.kotlinweatherapp.model.WeatherForAWeek
import com.example.kotlinweatherapp.room.NoteEntity

sealed class AppState {
    data class Success(val weatherData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
    data class SuccessWeek(val weatherForAWeek: List<WeatherForAWeek>) : AppState()
    data class ErrorWeek(val error: Throwable) : AppState()
    data class NoteLoaded(val note: String?) : AppState()
}
package com.example.kotlinweatherapp.viewmodel

import com.example.kotlinweatherapp.model.DailyWeather
import com.example.kotlinweatherapp.model.Weather

sealed class AppState {
    data class Success(val weatherData: Weather) : AppState()
    data class SuccessHistory(val weatherData: List<Weather>) : AppState()
    data class SuccessCityList(val weatherData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
    data class SuccessWeek(val weatherForAWeek: List<DailyWeather>) : AppState()
    data class ErrorWeek(val error: Throwable) : AppState()
    data class NoteLoaded(val note: String?) : AppState()
}
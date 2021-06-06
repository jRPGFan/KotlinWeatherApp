package com.example.kotlinweatherapp.utilities

import com.example.kotlinweatherapp.model.FactDTO
import com.example.kotlinweatherapp.model.Weather
import com.example.kotlinweatherapp.model.WeatherDTO
import com.example.kotlinweatherapp.model.getDefaultCity

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact: FactDTO = weatherDTO.fact!!
    return listOf(
        Weather(
            getDefaultCity(),
            fact.temp!!,
            fact.feels_like!!,
            fact.condition!!,
            fact.icon
        )
    )
}
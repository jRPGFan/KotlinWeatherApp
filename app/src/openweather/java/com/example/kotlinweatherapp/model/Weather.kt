package com.example.kotlinweatherapp.model

import android.os.Parcelable
import com.example.kotlinweatherapp.R
import kotlinx.android.parcel.Parcelize
import kotlin.random.Random

@Parcelize
data class Weather(
    val city: City = getDefaultCity(),
    var temperature: Double = 13.0,
    var feelsLike: Double = 11.0,
    val conditions: String = "clear",
    val icon: String? = "bkn_n",
    var hourlyWeather: List<HourlyWeather> = getNextSixHours(),
    val weatherForAWeek: List<DailyWeather> = getWeatherForAWeek()
) : Parcelable

//temperature = current.temp
//feelsLike = current.feels_like
//conditions = current.weather.main
//icon = current.weather.icon

fun getDefaultCity() = City("Кишинёв", 47.022778, 28.835278)

//fun getDefaultWeather() = WeatherConditions("Rain", R.drawable.rain)

private var list = listOf(
    WeatherConditions("Rainy", R.drawable.rain),
    WeatherConditions("Sunny", R.drawable.sunny),
    WeatherConditions("Cloudy", R.drawable.cloudy),
    WeatherConditions("Snowy", R.drawable.snowy)
)

var weatherList = mapOf(
    "Thunderstorm" to R.drawable.rain,
    "Drizzle" to R.drawable.rain,
    "Rain" to R.drawable.rain,
    "Snow" to R.drawable.snowy,
    "Atmosphere" to R.drawable.cloudy,
    "Clear" to R.drawable.sunny,
    "Clouds" to R.drawable.cloudy
)

fun getRandomWeather(): WeatherConditions {
    return list[Random.nextInt(0, 4)]
}

fun getWorldCities() = listOf(
    Weather(City("London", 51.507222, -0.1275), 1.0, 2.0, "clear"),
    Weather(City("Tokyo", 35.689722, 139.692222), 3.0, 4.0, "clear"),
    Weather(City("Paris", 48.856613, 2.352222), 5.0, 6.0, "clear"),
    Weather(City("Berlin", 52.52, 13.405), 7.0, 8.0, "clear"),
    Weather(City("Rome", 41.883333, 12.5), 9.0, 10.0, "clear"),
    Weather(City("Minsk", 53.9, 27.566667), 11.0, 12.0, "clear"),
    Weather(City("Istanbul", 41.013611, 28.955), 13.0, 14.0, "clear"),
    Weather(City("New York", 40.712778, -74.006111), 15.0, 16.0, "clear"),
    Weather(City("Kiev", 50.45, 30.523333), 17.0, 18.0, "clear"),
    Weather(City("Beijing", 39.906667, 116.3975), 19.0, 20.0, "clear"),
    Weather(City("Chisinau", 47.022778, 28.835278), 21.0, 15.0, "clear")
)

fun getRussianCities() = listOf(
    Weather(City("Москва", 55.755833, 37.617222), 1.0, 2.0, "clear"),
    Weather(City("Санкт-Петербург", 59.9375, 30.308611), 3.0, 3.0, "clear"),
    Weather(City("Новосибирск", 55.05, 82.95), 5.0, 6.0, "clear"),
    Weather(City("Екатеринбург", 56.835556, 60.612778), 7.0, 8.0, "clear"),
    Weather(City("Нижний Новгород", 56.326944, 44.0075), 9.0, 10.0, "clear"),
    Weather(City("Казань", 55.796389, 49.108889), 11.0, 12.0, "clear"),
    Weather(City("Челябинск", 55.154722, 61.375833), 13.0, 14.0, "clear"),
    Weather(City("Омск", 54.983333, 73.366667), 15.0, 16.0, "clear"),
    Weather(City("Ростов-на-Дону", 47.233333, 39.7), 17.0, 18.0, "clear"),
    Weather(City("Уфа", 54.733333, 56.0), 19.0, 20.0, "clear")
)
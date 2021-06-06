package com.example.kotlinweatherapp.model

import android.os.Parcelable
import com.example.kotlinweatherapp.R
import kotlinx.android.parcel.Parcelize
import kotlin.random.Random

@Parcelize
data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = 13,
    val feelsLike: Int = 11,
    val conditions: String = "clear",
    val icon: String? = "bkn_n",
    val weatherForAWeek: List<WeatherForAWeek> = getCityWeatherForAWeekLocalSource(),
) : Parcelable

fun getDefaultCity() = City("Кишинёв", 47.022778, 28.835278)

fun getDefaultWeather() = WeatherConditions("Rain", R.drawable.rain)

private var list = listOf(
    WeatherConditions("Rainy", R.drawable.rain),
    WeatherConditions("Sunny", R.drawable.sunny),
    WeatherConditions("Cloudy", R.drawable.cloudy),
    WeatherConditions("Snowy", R.drawable.snowy)
)

var weatherList = mapOf(
    "clear" to R.drawable.sunny,
    "partly-cloudy" to R.drawable.cloudy,
    "cloudy" to R.drawable.cloudy,
    "overcast" to R.drawable.cloudy,
    "drizzle" to R.drawable.rain,
    "light-rain" to R.drawable.rain,
    "rain" to R.drawable.rain,
    "moderate-rain" to R.drawable.rain,
    "heavy-rain" to R.drawable.rain,
    "continuous-heavy-rain" to R.drawable.rain,
    "showers" to R.drawable.rain,
    "wet-snow" to R.drawable.snowy,
    "light-snow" to R.drawable.snowy,
    "snow" to R.drawable.snowy,
    "snow-showers" to R.drawable.snowy,
    "hail" to R.drawable.snowy,
    "thunderstorm" to R.drawable.rain,
    "thunderstorm-with-rain" to R.drawable.rain,
    "thunderstorm-with-hail" to R.drawable.rain
)

fun getRandomWeather() : WeatherConditions {
    return list[Random.nextInt(0,4)]
}

fun getWorldCities() = listOf(
        Weather(City("London", 51.507222, -0.1275), 1, 2, "clear"),
        Weather(City("Tokyo", 35.689722, 139.692222), 3, 4, "clear"),
        Weather(City("Paris", 48.856613, 2.352222), 5, 6, "clear"),
        Weather(City("Berlin", 52.52, 13.405), 7, 8, "clear"),
        Weather(City("Rome", 41.883333, 12.5), 9, 10, "clear"),
        Weather(City("Minsk", 53.9, 27.566667), 11, 12, "clear"),
        Weather(City("Istanbul", 41.013611, 28.955), 13, 14, "clear"),
        Weather(City("New York", 40.712778, -74.006111), 15, 16, "clear"),
        Weather(City("Kiev", 50.45, 30.523333), 17, 18, "clear"),
        Weather(City("Beijing", 39.906667, 116.3975), 19, 20, "clear"),
        Weather(City("Chisinau", 47.022778, 28.835278), 21, 15, "clear")
)

fun getRussianCities() = listOf(
        Weather(City("Москва", 55.755833, 37.617222), 1, 2, "clear"),
        Weather(City("Санкт-Петербург", 59.9375, 30.308611), 3, 3, "clear"),
        Weather(City("Новосибирск", 55.05, 82.95), 5, 6, "clear"),
        Weather(City("Екатеринбург", 56.835556, 60.612778), 7,8, "clear"),
        Weather(City("Нижний Новгород", 56.326944, 44.0075), 9, 10, "clear"),
        Weather(City("Казань", 55.796389, 49.108889), 11, 12, "clear"),
        Weather(City("Челябинск", 55.154722, 61.375833), 13, 14, "clear"),
        Weather(City("Омск", 54.983333, 73.366667), 15, 16, "clear"),
        Weather(City("Ростов-на-Дону", 47.233333, 39.7), 17, 18, "clear"),
        Weather(City("Уфа", 54.733333, 56.0), 19, 20, "clear")
)
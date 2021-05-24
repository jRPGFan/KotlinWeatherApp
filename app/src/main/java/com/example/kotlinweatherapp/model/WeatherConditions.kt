package com.example.kotlinweatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherConditions(
    val description: String,
    val resID: Int
) : Parcelable
package com.example.kotlinweatherapp.view.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.model.DailyWeather
import com.example.kotlinweatherapp.model.weatherList

class DetailsFragmentAdapter : RecyclerView.Adapter<DetailsFragmentAdapter.DetailsViewHolder>() {

    private var weakWeatherData: List<DailyWeather> = listOf()

    fun setWeekWeather(data: List<DailyWeather>) {
        weakWeatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailsFragmentAdapter.DetailsViewHolder {

        return DetailsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_details_recycler_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: DetailsFragmentAdapter.DetailsViewHolder, position: Int) {
        holder.bind(weakWeatherData[position])
    }

    override fun getItemCount(): Int {
        return weakWeatherData.size
    }

    inner class DetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: DailyWeather) {
            itemView.apply {
                findViewById<TextView>(R.id.DaysContainerDayOfWeek).text =
                    weather.dayOfWeek
                findViewById<TextView>(R.id.DaysContainerDay).text = String.format(
                    resources.getString(R.string.week_day_temperature),
                    weather.dayTemp.toString()
                )
                findViewById<TextView>(R.id.DaysContainerNight).text = String.format(
                    resources.getString(R.string.week_night_temperature),
                    weather.nightTemp.toString()
                )
                findViewById<TextView>(R.id.DaysContainerWeatherCondition).text =
                    weather.conditions
                weatherList[weather.conditions]?.let {
                    findViewById<LinearLayout>(R.id.daysContainerLinearLayout)
                        .setBackgroundResource(it)
                }
            }
        }
    }
    //convert day to day of week
    // LocalDate.parse(weather.date).dayOfWeek.toString()
}
package com.example.kotlinweatherapp.view.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.model.WeatherForAWeek
import org.w3c.dom.Text

class DetailsFragmentAdapter : RecyclerView.Adapter<DetailsFragmentAdapter.DetailsViewHolder>() {

    private var weakWeatherData: List<WeatherForAWeek> = listOf()

    fun setWeekWeather(data: List<WeatherForAWeek>) {
        weakWeatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsFragmentAdapter.DetailsViewHolder {
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
        fun bind(weather: WeatherForAWeek) {
            itemView.findViewById<TextView>(R.id.DaysContainerDayOfWeek).text =
                weather.dayOfWeek.name
            itemView.findViewById<TextView>(R.id.DaysContainerDayTemperature).text =
                weather.dayTemperature.toString()
            itemView.findViewById<TextView>(R.id.DaysContainerNightTemperature).text =
                weather.nightTemperature.toString()
            itemView.findViewById<TextView>(R.id.DaysContainerWeatherCondition).text =
                weather.conditions.description
            itemView.findViewById<LinearLayout>(R.id.daysContainerLinearLayout)
                .setBackgroundResource(weather.conditions.resID)
        }
    }
}
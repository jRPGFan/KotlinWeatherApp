package com.example.kotlinweatherapp.view.details

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.FragmentDetailsBinding
import com.example.kotlinweatherapp.model.Weather
import com.example.kotlinweatherapp.model.WeatherDTO
import com.example.kotlinweatherapp.model.weatherList
import com.example.kotlinweatherapp.viewmodel.AppState
import com.example.kotlinweatherapp.viewmodel.DetailsViewModel
import com.google.android.material.snackbar.Snackbar

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val adapter = DetailsFragmentAdapter()

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    private val weatherBundle: Weather by lazy {
        arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
    }

    private val onLoadListener: WeatherLoader.WeatherLoaderListener = object : WeatherLoader.WeatherLoaderListener {
        override fun onLoaded(weatherDTO: WeatherDTO) {
            displayWeather(weatherDTO)
        }

        override fun onFailed(throwable: Throwable) {
            throwable.printStackTrace()
            throwable.message?.let {
                Snackbar.make(binding.mainView,
                    it, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loader = WeatherLoader(onLoadListener, weatherBundle.city.lat, weatherBundle.city.lon)
        loader.loadWeather()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessWeek -> {
                adapter.setWeekWeather(appState.weatherForAWeek)
            }
            is AppState.ErrorWeek -> {
                Snackbar.make(
                    binding.mainView, getString(R.string.error),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            is AppState.Loading -> {
                binding.mainView.visibility = View.VISIBLE
            }
            else -> Snackbar.make(
                binding.mainView, R.string.Unknown_Error,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun displayWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE
            val city = weatherBundle.city
            cityName.text = city.city
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
            weatherDescription.text = weatherDTO.fact?.condition
            temperatureValue.text = weatherDTO.fact?.temp.toString()
            feelsLikeValue.text = weatherDTO.fact?.feels_like.toString()
            weatherList[weatherDTO.fact?.condition]?.let { mainView.setBackgroundResource(it) }

            detailsFragmentRecyclerView.adapter = adapter

            viewModel.getLiveData().observe(viewLifecycleOwner, {
                renderData(it)
            })
            viewModel.getWeekWeatherFromLocalSource()
        }
    }
}
package com.example.kotlinweatherapp.view.details

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.load
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.FragmentDetailsBinding
import com.example.kotlinweatherapp.model.City
import com.example.kotlinweatherapp.model.Weather
import com.example.kotlinweatherapp.model.weatherList
import com.example.kotlinweatherapp.utilities.convertCelsiusToFahrenheit
import com.example.kotlinweatherapp.utilities.showSnackbarNoAction
import com.example.kotlinweatherapp.view.menu.IS_CELSIUS
import com.example.kotlinweatherapp.view.notes.NoteFragment
import com.example.kotlinweatherapp.viewmodel.AppState
import com.example.kotlinweatherapp.viewmodel.DetailsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val adapter = DetailsFragmentAdapter()
    private var isCelsius = true

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    private val weatherBundle: Weather by lazy {
        arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            isCelsius =
                it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_CELSIUS, true)
        }

        binding.cityNoteButton.setOnClickListener {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .add(NoteFragment.newInstance(Bundle().apply {
                        putString(NoteFragment.CITY_BUNDLE_EXTRA, weatherBundle.city.city)
                    }), "note")
                    .commit()
            }
        }

        binding.detailsFragmentRecyclerView.adapter = adapter
        viewModel.getLiveData().observe(viewLifecycleOwner, {
            renderData(it)
        })
        viewModel.getWeatherFromRemoteSource(weatherBundle.city.lat, weatherBundle.city.lon)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainView.visibility = View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                setWeather(appState.weatherData[0])
            }
            is AppState.Loading -> {
                binding.mainView.visibility = View.GONE
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainView.showSnackbarNoAction(
                    getString(R.string.Unknown_Error),
                    Snackbar.LENGTH_SHORT
                )
            }
            is AppState.SuccessWeek -> {
            }
            is AppState.ErrorWeek -> {
                Snackbar.make(
                    binding.mainView, getString(R.string.error),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            else -> {
                binding.mainView.showSnackbarNoAction(
                    getString(R.string.Unknown_Error), Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun setWeather(weather: Weather) {
        headerIcon.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")

        val city = weatherBundle.city
        var units = "C"

        if (!isCelsius) {
            weather.temperature = convertCelsiusToFahrenheit(weather.temperature)
            weather.feelsLike = convertCelsiusToFahrenheit(weather.feelsLike)
            units = "F"
        }

        saveCity(city, weather)
        with(binding) {
            cityName.text = city.city
            binding.cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(), city.lon.toString()
            )
            temperature.text = String.format(
                getString(R.string.temperature_label), weather.temperature.toString(), units
            )
            feelsLike.text = String.format(
                getString(R.string.feels_like_label), weather.feelsLike.toString(), units
            )

            weather.icon?.let {
                val svgImageLoader = ImageLoader.Builder(requireContext())
                    .componentRegistry {
                        add(coil.decode.SvgDecoder(requireContext()))
                    }
                    .build()

                weatherIcon.load(
                    "https://yastatic.net/weather/i/icons/blueye/color/svg/${it}.svg",
                    svgImageLoader
                )
            }

            weatherDescription.text = weather.conditions
            weatherList[weather.conditions]?.let { mainView.setBackgroundResource(it) }
            viewModel.getLiveData().observe(viewLifecycleOwner, {
                renderWeekData(it)
            })
            viewModel.getWeekWeatherFromLocalSource()
        }
    }

    private fun saveCity(city: City, weather: Weather) {
        viewModel.saveCityToDB(
            Weather(
                city,
                weather.temperature,
                weather.feelsLike,
                weather.conditions
            )
        )
    }

    private fun renderWeekData(appState: AppState) {
        when (appState) {
            is AppState.SuccessWeek -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
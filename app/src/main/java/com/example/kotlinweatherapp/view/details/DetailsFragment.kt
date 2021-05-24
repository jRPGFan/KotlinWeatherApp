package com.example.kotlinweatherapp.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.FragmentDetailsBinding
import com.example.kotlinweatherapp.model.Weather
import com.example.kotlinweatherapp.viewmodel.AppState
import com.example.kotlinweatherapp.viewmodel.DetailsViewModel
import com.example.kotlinweatherapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val adapter = DetailsFragmentAdapter()
    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA)
        if (weather != null) setData(weather)
        binding.detailsFragmentRecyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, {
            renderData(it) })
        viewModel.getWeekWeatherFromLocalSource()
    }

    private fun setData(weatherData: Weather) {
        binding.cityName.text = weatherData.city.city
        binding.cityCoordinates.text = String.format(
            binding.cityCoordinates.text.toString(),
            weatherData.city.lat.toString(), weatherData.city.lon.toString())

        binding.temperatureValue.text = weatherData.temperature.toString()
        binding.feelsLikeValue.text = weatherData.feelsLike.toString()
        binding.weatherDescription.text = weatherData.conditions.description
        binding.mainView.setBackgroundResource(weatherData.conditions.resID)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessWeek -> {
                adapter.setWeekWeather(appState.weatherForAWeek)
            }
            is AppState.ErrorWeek -> {
                Snackbar.make(binding.mainView, getString(R.string.error),
                    Snackbar.LENGTH_INDEFINITE).show()
            }
        }
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
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it)})
//        viewModel.getWeather()
//    }
//
//    private fun renderData(appState: AppState) {
//        when (appState) {
//            is AppState.Success -> {
//                val weatherData = appState.weatherData
//                binding.loadingLayout.visibility = View.GONE
//                setData(weatherData)
//            }
//            is AppState.Loading -> {
//                binding.loadingLayout.visibility = View.VISIBLE
//            }
//            is AppState.Error -> {
//                binding.loadingLayout.visibility = View.GONE
//                Snackbar
//                    .make(binding.mainView, "Error", Snackbar.LENGTH_INDEFINITE)
//                    .setAction("Reload") { viewModel.getWeather() }
//                    .show()
//            }
//        }
//    }
//
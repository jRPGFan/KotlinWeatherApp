package com.example.kotlinweatherapp.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.FragmentDetailsBinding
import com.example.kotlinweatherapp.model.Weather
import com.example.kotlinweatherapp.viewmodel.AppState
import com.example.kotlinweatherapp.viewmodel.DetailsViewModel
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
        arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?.let { weather ->
        weather.city.also { city ->
            binding.cityName.text = city.city
            binding.cityCoordinates.text = String.format(
                binding.cityCoordinates.text.toString(),
                city.lat.toString(), city.lon.toString()
            )
        }
            binding.temperatureValue.text = weather.temperature.toString()
            binding.feelsLikeValue.text = weather.feelsLike.toString()
            binding.weatherDescription.text = weather.conditions.description
            binding.mainView.setBackgroundResource(weather.conditions.resID)

            binding.detailsFragmentRecyclerView.adapter = adapter

            viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
            viewModel.getLiveData().observe(viewLifecycleOwner, {
                renderData(it) })
            viewModel.getWeekWeatherFromLocalSource()
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessWeek -> {
                adapter.setWeekWeather(appState.weatherForAWeek)
            }
            is AppState.ErrorWeek -> {
                Snackbar.make(binding.mainView, getString(R.string.error),
                    Snackbar.LENGTH_SHORT).show()
            }
            is AppState.Loading -> {
                binding.mainView.visibility = View.VISIBLE
            }
            else -> Snackbar.make(binding.mainView, "Unknown Error",
                        Snackbar.LENGTH_SHORT).show()
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
}
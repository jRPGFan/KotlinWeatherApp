package com.example.kotlinweatherapp.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.FragmentDetailsBinding
import com.example.kotlinweatherapp.model.FactDTO
import com.example.kotlinweatherapp.model.Weather
import com.example.kotlinweatherapp.model.WeatherDTO
import com.example.kotlinweatherapp.model.weatherList
import com.example.kotlinweatherapp.viewmodel.AppState
import com.example.kotlinweatherapp.viewmodel.DetailsViewModel
import com.google.android.material.snackbar.Snackbar

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_URL_MALFORMED_MESSAGE_EXTRA = "URL MALFORMED MESSAGE"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_TEMP_EXTRA = "TEMPERATURE"
const val DETAILS_FEELS_LIKE_EXTRA = "FEELS LIKE"
const val DETAILS_CONDITION_EXTRA = "CONDITION"
private const val TEMP_INVALID = -100
private const val FEELS_LIKE_INVALID = -100
private const val CONDITION_INVALID = "DREADFUL"
private const val PROCESS_ERROR = "ERROR PROCESSING"

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val adapter = DetailsFragmentAdapter()

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    private lateinit var weatherBundle: Weather
//    private val weatherBundle: Weather by lazy {
//        arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
//    }

    private val loadResultsReceiver: BroadcastReceiver = object :
        BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_DATA_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_SUCCESS_EXTRA -> renderData(
                    WeatherDTO(
                        FactDTO(
                            intent.getIntExtra(
                                DETAILS_TEMP_EXTRA, TEMP_INVALID
                            ),
                            intent.getIntExtra(
                                DETAILS_FEELS_LIKE_EXTRA,
                                FEELS_LIKE_INVALID
                            ),
                            intent.getStringExtra(
                                DETAILS_CONDITION_EXTRA
                            )
                        )
                    )
                )
                else -> TODO(PROCESS_ERROR)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(
                    loadResultsReceiver,
                    IntentFilter(DETAILS_INTENT_FILTER)
                )
        }
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
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
        binding.detailsFragmentRecyclerView.adapter = adapter
        getWeather()
    }

    private fun getWeather() {
        context?.let {
            it.startService(Intent(it, DetailsService::class.java).apply {
                putExtra(
                    LATITUDE_EXTRA,
                    weatherBundle.city.lat
                )
                putExtra(
                    LONGITUDE_EXTRA,
                    weatherBundle.city.lon
                )
            })
        }
    }

    private fun renderData(weatherDTO: WeatherDTO) {
        val fact = weatherDTO.fact
        val temp = fact?.temp ?: TEMP_INVALID
        val feelsLike = fact?.feels_like ?: FEELS_LIKE_INVALID
        val condition = fact?.condition ?: CONDITION_INVALID
        if (temp == TEMP_INVALID || feelsLike == FEELS_LIKE_INVALID ||
            condition == CONDITION_INVALID
        ) {
            TODO(PROCESS_ERROR)
        } else {
            val city = weatherBundle.city
            with(binding) {
                cityName.text = city.city
                cityCoordinates.text = String.format(
                    getString(R.string.city_coordinates),
                    city.lat.toString(),
                    city.lon.toString()
                )
                temperatureValue.text = temp.toString()
                feelsLikeValue.text = feelsLike.toString()
                weatherDescription.text = condition
                weatherList[condition]?.let { mainView.setBackgroundResource(it) }
                viewModel.getLiveData().observe(viewLifecycleOwner, {
                    renderWeekData(it)
                })
                viewModel.getWeekWeatherFromLocalSource()
            }
        }
    }

    private fun renderWeekData(appState: AppState) {
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
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
package com.example.kotlinweatherapp.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.FragmentMainBinding
import com.example.kotlinweatherapp.model.Weather
import com.example.kotlinweatherapp.utilities.showSnackbar
import com.example.kotlinweatherapp.utilities.showSnackbarNoAction
import com.example.kotlinweatherapp.view.details.DetailsFragment
import com.example.kotlinweatherapp.viewmodel.AppState
import com.example.kotlinweatherapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

private const val IS_WORLD_KEY = "LIST_OF_TOWNS_KEY"

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var isDataSetWorld: Boolean = false

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                    }))
                    .addToBackStack("")
                    .commit()
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }

        viewModel.getLiveData().observe(viewLifecycleOwner, {
            renderData(it)
        })
        viewModel.getWeatherFromLocalSourceRus()

        showListOfTowns()
    }

    private fun showListOfTowns() {
        activity?.let {
            if (it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_WORLD_KEY, false)) {
                changeWeatherDataSet()
            } else {
                viewModel.getWeatherFromLocalSourceRus()
            }
        }
    }

    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }

    private fun changeWeatherDataSet() =
        if (isDataSetWorld) {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        } else {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }.also {
            isDataSetWorld = !isDataSetWorld
            saveListOfTowns(isDataSetWorld)
        }

    private fun saveListOfTowns(isDataSetWorld: Boolean) {
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean(IS_WORLD_KEY, isDataSetWorld)
                apply()
            }
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
            }

            is AppState.Loading -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                binding.mainFragmentRootView.showSnackbar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getWeatherFromLocalSourceRus() })
            }
            else -> binding.mainFragmentRootView.showSnackbarNoAction(
                getString(R.string.Unknown_Error),
                Snackbar.LENGTH_SHORT
            )
        }
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }

    companion object {
        fun newInstance() =
            MainFragment()
    }
}
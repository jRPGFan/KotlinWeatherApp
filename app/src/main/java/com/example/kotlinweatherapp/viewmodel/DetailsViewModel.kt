package com.example.kotlinweatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.model.Repository
import com.example.kotlinweatherapp.model.RepositoryImpl

class DetailsViewModel (private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
                        private val repositoryImpl: Repository = RepositoryImpl()) : ViewModel() {
    fun getLiveData() = liveDataToObserve
    fun getWeekWeatherFromLocalSource() = getWeekDataFromLocalSource()

    private fun getWeekDataFromLocalSource(){
        liveDataToObserve.value = AppState.Loading

        Thread {
            liveDataToObserve.postValue(AppState.SuccessWeek(
                repositoryImpl.getCityWeatherForAWeekFromLocalStorage()))
        }.start()
    }
}
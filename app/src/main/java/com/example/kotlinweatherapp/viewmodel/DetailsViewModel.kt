package com.example.kotlinweatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.model.WeatherDTO
import com.example.kotlinweatherapp.repository.*
import com.example.kotlinweatherapp.utilities.convertDtoToModel
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

private const val SERVER_ERROR = "Server error"
private const val REQUEST_ERROR = "Request error"
private const val CORRUPTED_DATA = "Corrupted data"

class DetailsViewModel(
    private val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepository =
        DetailsRepositoryImpl(RemoteDataSource()),
    private val mainRepositoryImpl: MainRepository = MainRepositoryImpl()
) : ViewModel() {

    fun getLiveData() = detailsLiveData
    fun getWeekWeatherFromLocalSource() = getWeekDataFromLocalSource()

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveData.value = AppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(lat, lon, callBack)
    }

    private val callBack = object : Callback<WeatherDTO> {
        override fun onResponse(
            call: Call<WeatherDTO>,
            response: Response<WeatherDTO>
        ) {
            val serverResponse: WeatherDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            detailsLiveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: WeatherDTO): AppState {
            val fact = serverResponse.fact
            return if (fact?.temp == null || fact.feels_like == null || fact.condition.isNullOrEmpty()) {
                AppState.Error(Throwable(CORRUPTED_DATA))
            } else {
                AppState.Success(convertDtoToModel(serverResponse))
            }
        }
    }

    private fun getWeekDataFromLocalSource() {
        detailsLiveData.value = AppState.Loading

        Thread {
            detailsLiveData.postValue(
                AppState.SuccessWeek(
                    mainRepositoryImpl.getCityWeatherForAWeekFromLocalStorage()
                )
            )
        }.start()
    }
}


package com.example.kotlinweatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.app.App.Companion.getHistoryDao
import com.example.kotlinweatherapp.app.App.Companion.getNoteDao
import com.example.kotlinweatherapp.model.Weather
import com.example.kotlinweatherapp.model.WeatherDTO
import com.example.kotlinweatherapp.repository.*
import com.example.kotlinweatherapp.room.NoteEntity
import com.example.kotlinweatherapp.utilities.convertDtoToModel
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

private const val SERVER_ERROR = "Server error"
private const val REQUEST_ERROR = "Request error"
private const val CORRUPTED_DATA = "Corrupted data"

class DetailsViewModel(
    private val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepository: DetailsRepository =
        DetailsRepositoryImpl(RemoteDataSource()),
    private val mainRepository: MainRepository = MainRepositoryImpl(),
    private val historyRepository: LocalRepository = LocalRepositoryImpl(getHistoryDao())
) : ViewModel() {

    fun getLiveData() = detailsLiveData
    fun getWeekWeatherFromLocalSource() = getWeekDataFromLocalSource()

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveData.value = AppState.Loading
        detailsRepository.getWeatherDetailsFromServer(lat, lon, callBack)
    }

    fun saveCityToDB(weather: Weather) {
        historyRepository.saveEntity(weather)
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
                    mainRepository.getCityWeatherForAWeekFromLocalStorage()
                )
            )
        }.start()
    }
}


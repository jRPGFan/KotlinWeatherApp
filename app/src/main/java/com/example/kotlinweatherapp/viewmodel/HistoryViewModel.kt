package com.example.kotlinweatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapp.app.App.Companion.getHistoryDao
import com.example.kotlinweatherapp.repository.LocalRepository
import com.example.kotlinweatherapp.repository.LocalRepositoryImpl

class HistoryViewModel (val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
private val historyRepository: LocalRepository = LocalRepositoryImpl(getHistoryDao())
 ) :ViewModel() {
     fun getAllHistory() {
         historyLiveData.value = AppState.Loading
         historyLiveData.value = AppState.Success(historyRepository.getAllHistory())
     }
 }
package com.example.presentapp2.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PresentViewModel(application: Application): AndroidViewModel(application) {
    val readAllData : LiveData<List<Present>>
    private val repository : PresentRepository

    init {
        val presentDao = PresentDatabase.getDatabase(application).presentDao()

        repository = PresentRepository(presentDao)
        readAllData = repository.readAllData
    }

    fun addPresent(present: Present){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPresent(present)
        }
    }
}
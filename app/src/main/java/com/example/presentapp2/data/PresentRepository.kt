package com.example.presentapp2.data

import androidx.lifecycle.LiveData

class PresentRepository(private val presentDao: PresentDao) {
    val readAllData : LiveData<List<Present>> = presentDao.readAllData()

    fun addPresent(present: Present){
        presentDao.addPresent(present)
    }
}
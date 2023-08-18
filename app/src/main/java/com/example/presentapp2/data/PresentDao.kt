package com.example.presentapp2.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PresentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPresent(present: Present)

    @Query("SELECT * FROM present_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Present>>
}
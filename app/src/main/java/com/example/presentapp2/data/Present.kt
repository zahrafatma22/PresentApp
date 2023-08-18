package com.example.presentapp2.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "present_table")
data class Present(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val tgl: String,
    val jam: String,
    val status: String

) :Parcelable

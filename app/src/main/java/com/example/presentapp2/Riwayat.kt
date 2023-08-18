package com.example.presentapp2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.presentapp2.R
import com.example.presentapp2.data.Present
import com.example.presentapp2.data.PresentViewModel

class Riwayat : AppCompatActivity() {

    private lateinit var  mPresentViewModel: PresentViewModel
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        val adapter = PresentListAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.tv_rv_riwayat)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        mPresentViewModel = ViewModelProvider(this).get(PresentViewModel::class.java)
        val observer = Observer<List<Present>> { present ->
            // Update the UI or perform actions based on the LiveData changes
            adapter.setData(present)
        }
        mPresentViewModel.readAllData.observeForever(observer)
    }
}
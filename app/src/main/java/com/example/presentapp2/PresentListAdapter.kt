package com.example.presentapp2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.presentapp2.data.Present

class PresentListAdapter : RecyclerView.Adapter<PresentListAdapter.MyViewHolder>() {
    private var presensiList = emptyList<Present>()
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.costum_row,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = presensiList[position]
        holder.itemView.findViewById<TextView>(R.id.tv_tgl).text = currentItem.tgl
        holder.itemView.findViewById<TextView>(R.id.tv_jam).text = currentItem.jam
        holder.itemView.findViewById<TextView>(R.id.tv_status).text = currentItem.status


    }

    override fun getItemCount(): Int {

        return presensiList.size
    }

    fun setData(presensi : List<Present>){
        this.presensiList = presensi
        notifyDataSetChanged()
    }
}
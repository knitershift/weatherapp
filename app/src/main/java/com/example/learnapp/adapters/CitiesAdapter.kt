package com.example.learnapp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.learnapp.activities.MainActivity
import com.example.learnapp.R
import com.example.learnapp.models.CityInfo
import kotlinx.android.synthetic.main.list_item_city.view.*

class CitiesAdapter(var items: ArrayList<CityInfo>, val context: Context, val activity: MainActivity, val onItemClickListener: View.OnClickListener):
    RecyclerView.Adapter<CitiesAdapter.MyViewHolder>()
{



    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var item:View = view
        var tvCityName: TextView = view.tvCityName
        var tvTemperature: TextView = view.tvTemperature

        init {
            view.setOnClickListener(onItemClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_item_city, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {

        val obj = items[position]
        holder.item.tag = obj
        holder.tvCityName.text = obj.nameUkr
    }

    override fun getItemCount(): Int
    {
        return items.size
    }
}
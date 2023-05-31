package com.example.carepro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DetailsAdapter(private var daily: MutableList<Daily> = mutableListOf<Daily>()):RecyclerView.Adapter<DetailsAdapter.ViewHolder2>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsAdapter.ViewHolder2 {
        val itemView = LayoutInflater.from(parent.context)
        val DailyList= itemView.inflate(R.layout.history, parent, false)

        return DetailsAdapter.ViewHolder2(DailyList)
    }

    override fun onBindViewHolder(holder: DetailsAdapter.ViewHolder2, position: Int) {
        val today = daily[position]
        holder.customBind(today)
    }

    override fun getItemCount(): Int {
       return daily.size
    }

    class ViewHolder2(private var itemView: View): RecyclerView.ViewHolder(itemView) {

        fun customBind(obj:Daily){


            val fooddetails:TextView=itemView.findViewById(R.id.FoodName)
            val calorieetails:TextView=itemView.findViewById(R.id.Calories)

            var fooditem=obj.foodname.toString().trim()
            var calorie=obj.calories.toString().trim()

            fooddetails.text=fooditem
            calorieetails.text=(calorie+" kcal")

        }

    }

}
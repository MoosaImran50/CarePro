package com.example.carepro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DailyAdapter(private var daily: MutableList<Daily> = mutableListOf<Daily>()):RecyclerView.Adapter<DailyAdapter.ViewHolder>() {


    private lateinit var rListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        rListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        val DailyList= itemView.inflate(R.layout.history2, parent, false)

        return ViewHolder(DailyList,rListener)

    }

    override fun onBindViewHolder(holder: DailyAdapter.ViewHolder, position: Int) {
        val today = daily[position]
        holder.customBind(today)
    }

    override fun getItemCount(): Int {
        return  daily.size
    }

    class ViewHolder(private var itemView: View,listener:OnItemClickListener): RecyclerView.ViewHolder(itemView) {
        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
        fun customBind(obj:Daily){

//            var currentday: TextView = itemView.findViewById(R.id.textView3)
//            var currentdate:TextView=itemView.findViewById(R.id.textView2)
//            currentday.text=obj.currentday.toString()
//            currentdate.text=obj.currentdate.toString()

            val today:TextView=itemView.findViewById(R.id.record)

             var currentday=obj.currentday.toString().trim()
             var currentdate=obj.currentdate.toString().trim()
             var currentmonth=obj.currentmonth.toString().trim()

            var display=" ${currentmonth.uppercase()} $currentdate $currentday"

            today.text=display

        }

    }

}
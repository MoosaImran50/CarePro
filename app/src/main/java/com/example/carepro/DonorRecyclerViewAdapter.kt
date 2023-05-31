package com.example.carepro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DonorRecyclerViewAdapter: RecyclerView.Adapter<DonorRecyclerViewAdapter.MyViewHolder>(){

    private val donorsList = ArrayList<Donor>()

    // creating OnItemClickListener class object
    private lateinit var mListener: OnItemClickListener

    // on OnItemClickListener class interface
    interface OnItemClickListener {
        // onItemClick function that will be override from ContactActivity
        fun onItemClick(position: Int)
    }

    // function that will be called to override onItemClick function of OnItemClickListener interface
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.donor_list_item, parent, false)

        return  MyViewHolder(listItem, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val donor = donorsList[position]
        holder.bind(donor)
    }

    override fun getItemCount(): Int {
        return donorsList.size
    }


    fun updateDonorList(donorList: List<Donor>){
        this.donorsList.clear()
        this.donorsList.addAll(donorList)
        notifyDataSetChanged()
    }


    class MyViewHolder(private val view: View, listener: OnItemClickListener): RecyclerView.ViewHolder(view){
        // setting onclick listener for the particular view
        init{
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        fun bind(donor: Donor){
            val bloodGroup = view.findViewById<TextView>(R.id.bloodGroup)
            val location = view.findViewById<TextView>(R.id.location)
            val contact = view.findViewById<TextView>(R.id.contact)

            bloodGroup.text = donor.blood_group
            location.text = donor.location
            contact.text = donor.contact_id
        }

    }
}
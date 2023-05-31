package com.example.carepro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carepro.Donor
import com.example.carepro.DonorDetails
import com.example.carepro.DonorRecyclerViewAdapter
import com.example.carepro.DonorViewModel
import kotlin.collections.ArrayList

class BloodDonorsList : AppCompatActivity() {

    private lateinit var viewModel: DonorViewModel
    private lateinit var donorRecyclerView: RecyclerView
    lateinit var adapter: DonorRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blood_donors_list)
        getSupportActionBar()?.setTitle("Donors List")

        // setting up RecyclerView
        donorRecyclerView = findViewById<RecyclerView>(R.id.DonorsRecyclerView)
        donorRecyclerView.layoutManager = LinearLayoutManager(this)
        donorRecyclerView.setHasFixedSize(true)
        donorRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        adapter = DonorRecyclerViewAdapter()
        donorRecyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(DonorViewModel::class.java)

        val donorsList = viewModel.allDonors

        donorsList.observe(this, Observer {
            adapter.updateDonorList(it)
        })

        adapter.setOnItemClickListener(object: DonorRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {

                val intent = Intent(this@BloodDonorsList, DonorDetails::class.java)

                intent.putExtra("DonorName", donorsList.value?.get(position)?.name)
                intent.putExtra("DonorBloodGroup", donorsList.value?.get(position)?.blood_group)
                intent.putExtra("DonorLocation", donorsList.value?.get(position)?.location)
                intent.putExtra("DonorAge", donorsList.value?.get(position)?.age.toString())
                intent.putExtra("DonorGender", donorsList.value?.get(position)?.gender)
                intent.putExtra("DonorContact", donorsList.value?.get(position)?.contact_id)

                startActivity(intent)

            }
        })

        val searchTextBox = findViewById<SearchView>(R.id.searchDonorTextBox)

        searchTextBox.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val filterPattern = newText?.trim()?.toLowerCase()
                val filteredList = ArrayList<Donor>()

                if (filterPattern.isNullOrEmpty()) {

                    filteredList.addAll(viewModel.allDonors.value ?: emptyList())

                }
                else {

                    viewModel.allDonors.value?.forEach { donor ->

                        if (donor.name?.toLowerCase()?.contains(filterPattern) == true ||
                            donor.blood_group?.toLowerCase()?.contains(filterPattern) == true ||
                            donor.location?.toLowerCase()?.contains(filterPattern) == true) {

                            filteredList.add(donor)

                        }

                    }

                }

                adapter.updateDonorList(filteredList)
                return true
            }

        })


    }

}
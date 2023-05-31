package com.example.carepro

import androidx.lifecycle.MutableLiveData

interface IFirebaseRepository {

    // reading donors from database
    fun readDonors(donorList: MutableLiveData<List<Donor>>)

    // inserting donor into database
    fun addDonor(donor: Donor)

}
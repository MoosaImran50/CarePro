package com.example.carepro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DonorViewModel: ViewModel() {

    private val repository: IFirebaseRepository
    private val donors = MutableLiveData<List<Donor>>()

    val allDonors: LiveData<List<Donor>> = donors

    init {
        repository = FirebaseRepository().getInstance()
        repository.readDonors(donors)
    }

}
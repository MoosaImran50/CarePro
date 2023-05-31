package com.example.carepro

import com.example.carepro.FirebaseRepository
import com.example.carepro.IFirebaseRepository

class Donor(var contact_id: String ?= null,
            var name: String ?= null,
            var blood_group: String ?= null,
            var location: String ?= null,
            var age: Int ?= null,
            var gender: String ?= null,
            ){

    fun addDonor(){
        val repository: IFirebaseRepository
        repository = FirebaseRepository().getInstance()
        repository.addDonor(this)
    }

}

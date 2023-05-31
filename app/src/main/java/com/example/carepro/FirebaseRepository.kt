package com.example.carepro

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

class FirebaseRepository(): IFirebaseRepository {

    private val donorsDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Donors")

    @Volatile private var INSTANCE: FirebaseRepository ?= null

    fun getInstance(): FirebaseRepository{

        return INSTANCE ?: synchronized(this){
            val instance = FirebaseRepository()
            INSTANCE = instance
            instance
        }

    }

    // reading contacts from database
    override fun readDonors(donorList: MutableLiveData<List<Donor>>) {

        val contactQuery = donorsDatabaseReference.orderByChild("blood_group")

        contactQuery.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val donors: List<Donor> = snapshot.children.map { dataSnapshot ->
                    dataSnapshot.getValue(Donor::class.java)!!
                }

                donorList.postValue(donors)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseHelper", "Cancelled")
            }

        })

        donorsDatabaseReference.keepSynced(true)

    }

    // inserting contact into database
    override fun addDonor(donor: Donor) {

        val newMessageId = donorsDatabaseReference.push().key

        donorsDatabaseReference.child(newMessageId!!).setValue(donor).addOnSuccessListener {
            Log.d("DatabaseHelper", "Insert Success")
        }.addOnFailureListener{
            Log.d("DatabaseHelper", "Insert Failed")
        }

    }

}
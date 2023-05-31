package com.example.carepro

import android.app.Application
import com.google.firebase.database.FirebaseDatabase


class CarePro: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

}
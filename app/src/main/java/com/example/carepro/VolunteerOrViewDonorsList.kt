package com.example.carepro

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class VolunteerOrViewDonorsList : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var context: Context = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.volunteer_or_view_donors_list)
        getSupportActionBar()?.setTitle("Volunteer Or View Donors List")

        loadInter()

        val donorsListButton = findViewById<Button>(R.id.buttonDonorsList)
        val volunteerButton = findViewById<Button>(R.id.buttonVolunteer)

        donorsListButton.setOnClickListener{
            val intent = Intent(this@VolunteerOrViewDonorsList, BloodDonorsList::class.java)
            startActivity(intent)
        }

        volunteerButton.setOnClickListener{
            val intent = Intent(this@VolunteerOrViewDonorsList, BloodVolunteer::class.java)
            startActivity(intent)
        }

        val adRequest = AdRequest.Builder().build()
        MobileAds.initialize(context) {}
        val mAdView: AdView = findViewById<com.google.android.gms.ads.AdView>(R.id.adView)
        mAdView.loadAd(adRequest)

        InterstitialAd.load(context, "ca-app-pub-3940256099942544/1033173712", adRequest, object: InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                mInterstitialAd?.show(context as Activity)
            }

        })

    }

    private fun loadInter(){
        if (mInterstitialAd != null){
            mInterstitialAd?.show(this)
        }
        else{
            Log.d("TAG", "Ad was monki!")
        }
    }
}
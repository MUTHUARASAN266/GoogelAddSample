package com.example.googleadsample

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.googleadsample.databinding.ActivityInterstitialAdScreenBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class InterstitialAdScreen : AppCompatActivity() {
    private var interstitialAd: InterstitialAd? = null
    lateinit var binding: ActivityInterstitialAdScreenBinding
    private val TAG = "InterstitialAdScreen"
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterstitialAdScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdview.setOnClickListener {
            startActivity(Intent(this@InterstitialAdScreen, RewardedVideoAdScreen::class.java))
            finish()
        }

        MobileAds.initialize(this) { initializationStatus ->
            // Initialization completed
            Log.e(TAG, "initializationStatus: ${initializationStatus.adapterStatusMap}")
            Log.e(TAG, "initializationStatus values : ${initializationStatus.adapterStatusMap.values}")
        }

        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Loading Ad...")
        progressDialog?.setCancelable(false)

        loadInterstitialAd()

        // You can trigger showing the interstitial ad at an appropriate point in your app, for example, in a button click.
        // For demonstration purposes, we will show the ad when the app starts.
    }

    private fun loadInterstitialAd() {
        progressDialog?.show() // Show progress dialog while loading the ad

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this@InterstitialAdScreen, getString(R.string.interstitial_ad_unit_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
                showInterstitialAd()
                progressDialog?.dismiss() // Dismiss progress dialog
                Log.e(TAG, "AdLoaded: ${ad.adUnitId}")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                progressDialog?.dismiss() // Dismiss progress dialog
                interstitialAd = null
                Log.e(TAG, "AdFailedToLoad: ${adError.message}")
            }
        })
    }

    private fun showInterstitialAd() {
        interstitialAd?.show(this)
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                // Ad was dismissed
                // Proceed with the next action in your app
                Log.e(TAG, "Ad was dismissed")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Ad failed to show
                Log.e(TAG, "Ad failed: ${adError.message}")
            }

            override fun onAdShowedFullScreenContent() {
                // Ad was shown
                Log.e(TAG, "Ad was showing")
            }
        }
    }
}
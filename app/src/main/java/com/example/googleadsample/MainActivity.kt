package com.example.googleadsample

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.googleadsample.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) // Initialize MobileAds

        binding.adview.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.d(TAG, "AdClosed.")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                Log.e(TAG, "Ad failed to load: ${adError.message}")
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d(TAG, "Ad loaded successfully.")
                progressDialog.dismiss() // Dismiss the ProgressDialog when the ad is loaded.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that covers the screen.
            }
        }

        binding.btnInterstitialAd.setOnClickListener {
            startActivity(Intent(this@MainActivity, InterstitialAdScreen::class.java))
            finish()
        }

        // Initialize and show the ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading ad...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        getDeviceAdId()
    }

    private fun getDeviceAdId() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(applicationContext)
                val advertisingId = adInfo.id
                val isLimitAdTrackingEnabled = adInfo.isLimitAdTrackingEnabled
                Log.d(TAG, "advertisingId: $advertisingId")
                Log.d(TAG, "isLimitAdTrackingEnabled: $isLimitAdTrackingEnabled")

                val requestConfiguration = RequestConfiguration.Builder()
                    .setTestDeviceIds(listOf(advertisingId))
                    .build()
                MobileAds.setRequestConfiguration(requestConfiguration)
            } catch (e: Exception) {
                Log.e(TAG, "Google Play Services not available: $e")
            }

            val adRequest = AdRequest.Builder().build()
            runOnUiThread {
                binding.adview.loadAd(adRequest)
            }
        }
    }
}

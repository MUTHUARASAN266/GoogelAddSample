package com.example.googleadsample

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.googleadsample.databinding.ActivityRewardedVideoAdScreenBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.snackbar.Snackbar

class RewardedVideoAdScreen : AppCompatActivity(R.layout.activity_rewarded_video_ad_screen) {
    private lateinit var binding: ActivityRewardedVideoAdScreenBinding
    private var rewardedAd: RewardedAd? = null
    private val TAG = "RewardedVideoAdScreen"
    private var loadingDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardedVideoAdScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {
            // Initialization completed
            showLoadingDialog()
            loadRewardedAd()
        }
    }

    private fun showLoadingDialog() {
        loadingDialog = ProgressDialog(this)
        loadingDialog?.setMessage("Loading Ad...")
        loadingDialog?.setCancelable(false)
        loadingDialog?.show()
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(this, getString(R.string.reward_ad_unit_id), adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Ad failed to load, handle the error gracefully
                Log.e(TAG, "AdFailedToLoad: ${adError.message}")
                dismissLoadingDialog()
            }

            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
                Log.d(TAG, "onAdLoaded: adUnitId->${ad.adUnitId}")
                Log.d(TAG, "onAdLoaded: rewardItem->${ad.rewardItem}")
                Log.d(TAG, "onAdLoaded: adMetadata->${ad.adMetadata}")
                dismissLoadingDialog()
                showRewardedAd()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showRewardedAd() {
        if (rewardedAd != null) {
            rewardedAd?.show(this) { rewardItem ->
                // The user has earned a reward
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type

                // Handle the reward
                Log.d(TAG, "Reward Amount: $rewardAmount")
                Snackbar.make(binding.root, "Earned: $rewardAmount $rewardType", Snackbar.LENGTH_LONG).show()
                binding.result.text= rewardType
                Log.d(TAG, "Reward Type: $rewardType")
            }
        } else {
            // The rewarded ad is not ready yet
            Log.e(TAG, "The rewarded ad is not ready yet")
        }
    }
}



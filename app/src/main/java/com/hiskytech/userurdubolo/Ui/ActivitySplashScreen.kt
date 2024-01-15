package com.hiskytech.userurdubolo.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.ArrayList
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.admin.Utils
import com.hiskytech.userurdubolo.Data.SharedPrefManager
import com.hiskytech.userurdubolo.Model.ModelDrama
import com.hiskytech.userurdubolo.Model.ModelSeason
import com.hiskytech.userurdubolo.Model.ModelVideo
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.ViewModel.DramaViewModel
import com.hiskytech.userurdubolo.ViewModel.SeasonViewModel
import com.hiskytech.userurdubolo.ViewModel.UserViewModel
import com.hiskytech.userurdubolo.ViewModel.VideoViewModel
import kotlinx.coroutines.launch

class ActivitySplashScreen : AppCompatActivity() {
    private lateinit var sharedPrefManager: SharedPrefManager
    private val userViewModel: UserViewModel by viewModels()
    private val videoViewModel: VideoViewModel by viewModels()
    private val dramaviewModel: DramaViewModel by viewModels()
    private val seasonViewModel: SeasonViewModel by viewModels()
    private lateinit var utils:Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        actionBar?.hide()
        supportActionBar?.hide()
utils=Utils(this@ActivitySplashScreen)
        sharedPrefManager = SharedPrefManager(this@ActivitySplashScreen)

        // 4.5 seconds delay (3s animation + 1.5s Timer delay)
        retrieveAndStoreLists()
    }


    private fun retrieveAndStoreLists() {
        var videoList = ArrayList<ModelVideo>()
        var dramaList = ArrayList<ModelDrama>()
        var hudutsuzVideoList = ArrayList<ModelVideo>()
        var alllSeasonList = ArrayList<ModelSeason>()
        var videoListPublic = ArrayList<ModelVideo>()

        lifecycleScope.launch {

            videoViewModel.getPrivateVideoList()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            videoList.add(document.toObject(ModelVideo::class.java))
                        }
                        sharedPrefManager.putPrivateVideoList(videoList)

                        lifecycleScope.launch {
                            dramaviewModel.getDramalist()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        for (document in task.result) {
                                            dramaList.add(document.toObject(ModelDrama::class.java))
                                        }
                                        sharedPrefManager.putDramaList(dramaList)

                                        lifecycleScope.launch {
                                            seasonViewModel.getHudutsuzVideoList("Hudutsuz sevda")
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        for (document in task.result) {
                                                            hudutsuzVideoList.add(
                                                                document.toObject(
                                                                    ModelVideo::class.java
                                                                )
                                                            )
                                                        }
                                                        sharedPrefManager.putHudutsuzVideoList(
                                                            hudutsuzVideoList
                                                        )
                                                        lifecycleScope.launch {
                                                                            seasonViewModel.getAllSeasons()
                                                                                .addOnCompleteListener { task ->
                                                                                    if (task.isSuccessful) {
                                                                                        for (document in task.result) {
                                                                                            alllSeasonList.add(
                                                                                                document.toObject(
                                                                                                    ModelSeason::class.java
                                                                                                )
                                                                                            )
                                                                                        }
                                                                                        sharedPrefManager.putSeasonList(
                                                                                            alllSeasonList
                                                                                        )
                                                                                        lifecycleScope.launch {
                                                                                            videoViewModel.getPublicVideoList()
                                                                                                .addOnCompleteListener { task ->
                                                                                                    if (task.isSuccessful) {
                                                                                                        for (document in task.result) {
                                                                                                            videoListPublic.add(
                                                                                                                document.toObject(
                                                                                                                    ModelVideo::class.java
                                                                                                                )
                                                                                                            )
                                                                                                        }
                                                                                                        sharedPrefManager.putPublicVideoList(
                                                                                                            videoListPublic
                                                                                                        )

                                                                                                        handleActivityOpening()
                                                                                                    }
                                                                                                }
                                                                                                .addOnFailureListener {
                                                                                                    // Handle failure
                                                                                                }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                .addOnFailureListener {
                                                                                    // Handle failure
                                                                                }
                                                                        }
                                                                    }
                                                                }
                                                                .addOnFailureListener {
                                                                    // Handle failure
                                                                }
                                                        }
                                                    }
                                                }
                                                .addOnFailureListener {
                                                    // Handle failure
                                                }
                                        }
                                    }
                                }
                        }

        }


                                    private fun handleActivityOpening() {
                                        if (sharedPrefManager.isLoggedIn()) {
                                            startActivity(
                                                Intent(
                                                    this@ActivitySplashScreen,
                                                    MainActivity::class.java
                                                )
                                            )
                                        } else {
                                            startActivity(
                                                Intent(
                                                    this@ActivitySplashScreen,
                                                    ActivityIntro::class.java
                                                )
                                            )
                                        }
                                        finish()
                                    }
                                }
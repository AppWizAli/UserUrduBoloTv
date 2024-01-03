package com.hiskytech.userurdubolo.Ui
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.hiskytech.userurdubolo.Data.SharedPrefManager
import com.hiskytech.userurdubolo.Model.ModelDrama
import com.hiskytech.userurdubolo.Model.ModelSeason
import com.hiskytech.userurdubolo.Model.ModelUser
import com.hiskytech.userurdubolo.Model.ModelVideo
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.ViewModel.DramaViewModel
import com.hiskytech.userurdubolo.ViewModel.SeasonViewModel
import com.hiskytech.userurdubolo.ViewModel.UserViewModel
import com.hiskytech.userurdubolo.ViewModel.VideoViewModel
import kotlinx.coroutines.launch

class ActivitySplash : AppCompatActivity() {
    private lateinit var sharedPrefManager: SharedPrefManager
    private val userViewModel: UserViewModel by viewModels()
    private val videoViewModel: VideoViewModel by viewModels()
    private val dramaviewModel: DramaViewModel by viewModels()
    private val seasonViewModel: SeasonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        actionBar?.hide()
        supportActionBar?.hide()

        sharedPrefManager = SharedPrefManager(this@ActivitySplash)

        val logoImageView: ImageView = findViewById(R.id.imageView)
        val fadeIn = AlphaAnimation(0.0f, 1.0f)
        fadeIn.duration = 2000
        fadeIn.fillAfter = true

        val fadeOut = AlphaAnimation(1.0f, 0.0f)
        fadeOut.startOffset = 1000
        fadeOut.duration = 1
        fadeOut.fillAfter = true
        fadeOut.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}

            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
            }

            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        })
        logoImageView.startAnimation(fadeIn)
        logoImageView.visibility = ImageView.VISIBLE

        Handler().postDelayed({
            logoImageView.startAnimation(fadeOut)
        }, 7000) // 3 seconds delay

        Handler().postDelayed({
            handleActivityOpening()
        }, 7000) // 4.5 seconds delay (3s animation + 1.5s Timer delay)
    }

    private fun handleActivityOpening() {
        storeList()
        if (sharedPrefManager.isLoggedIn()) {
            startActivity(Intent(this@ActivitySplash, MainActivity::class.java))
        } else {
            startActivity(Intent(this@ActivitySplash, ActivityIntro::class.java))
        }
        finish()
    }

    private fun storeList() {


        val videoList = ArrayList<ModelVideo>()
        lifecycleScope.launch {
            // Fetch and store video list
            videoViewModel.getPrivateVideoList()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result.size() > 0) {
                            for (document in task.result)
                                videoList.add(document.toObject(ModelVideo::class.java))

                            sharedPrefManager.putPrivateVideoList(videoList)
                        }
                    }
                }
                .addOnFailureListener {
                    // Handle failure
                }
        }
        val dramaList = ArrayList<ModelDrama>()
        lifecycleScope.launch {
            // Fetch and store video list
            dramaviewModel.getDramalist()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result.size() > 0) {
                            for (document in task.result)
                                dramaList.add(document.toObject(ModelDrama::class.java))

                            sharedPrefManager.putDramaList(dramaList)
                        }
                    }
                }
                .addOnFailureListener {
                    // Handle failure
                }
        }
        val HudutsuzSeasonList = ArrayList<ModelSeason>()
        lifecycleScope.launch {
            // Fetch and store video list
            seasonViewModel.getHudutsuzSeasonList("Hudutsuz")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result.size() > 0) {
                            for (document in task.result){
                                HudutsuzSeasonList.add(document.toObject(ModelSeason::class.java))
                            }

                            Toast.makeText(this@ActivitySplash, "saved", Toast.LENGTH_SHORT).show()
                            sharedPrefManager.putHudutsuzSeasonList(HudutsuzSeasonList)
                        }
                        else
                        {
                            Toast.makeText(this@ActivitySplash, "Zero size", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                .addOnFailureListener {
                    // Handle failure
                }
        }
    }
}

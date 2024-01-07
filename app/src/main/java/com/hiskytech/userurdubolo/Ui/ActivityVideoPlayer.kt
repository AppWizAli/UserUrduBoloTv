package com.hiskytech.userurdubolo.Ui

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.admin.Utils
import com.hiskytech.userurdubolo.Data.SharedPrefManager
import com.hiskytech.userurdubolo.Model.ModelVideo
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.ViewModel.UserViewModel
import com.hiskytech.userurdubolo.databinding.ActivityVideoPlayerBinding


class ActivityVideoPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityVideoPlayerBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var utils: Utils
    private lateinit var webView: WebView
    private var dialog: Dialog? = null
    private var handler: Handler? = null
    private lateinit var modelVideo: ModelVideo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        supportActionBar?.hide()
        binding=ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        utils= Utils(this@ActivityVideoPlayer)
        sharedPrefManager= SharedPrefManager(this@ActivityVideoPlayer)
        modelVideo=ModelVideo()
        val receivedIntent = intent
        val modelDramaString = receivedIntent.getStringExtra("Video")
      modelVideo = ModelVideo.fromString(modelDramaString!!)!!

        dialog = Dialog(this)
        dialog!!.setContentView(R.layout.customdialog)
        dialog!!.setCancelable(false)
        handler = Handler()
        dismissDialogAfterDelay(5000)
        webView = findViewById(R.id.webView)
        if (!isInternetAvailable()) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            finish() // Close the activity if there is no internet
            return
        }

        val webSettings = webView.getSettings()
        webSettings.javaScriptEnabled = true
        val driveLink =modelVideo.videourl
        val videoId = driveLink.substringAfterLast("/d/").substringBefore("/view")
        val videoUrl = "https://drive.google.com/file/d/$videoId/preview"

        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url) // Load the URL inside the WebView
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                view.loadUrl(
                    "javascript:(function() { " +
                            "var shareButton = document.querySelector('.ndfHFb-c4YZDc-Wrql6b'); " +
                            "if (shareButton) shareButton.style.display='none'; " +
                            "var downloadButton = document.querySelector('.ndfHFb-c4YZDc-MZArnb-Q9KMQd'); " +
                            "if (downloadButton) downloadButton.style.display='none'; " +
                            "})()"
                )
            }
        })
        webView.loadUrl(videoUrl)
        showDialog()
    }
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val networkInfo = connectivityManager?.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
    private fun showDialog() {
        if (dialog != null && !dialog!!.isShowing) {
            dialog!!.show()
        }
    }

    private fun dismissDialog() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }
    private fun dismissDialogAfterDelay(delayMillis: Long) {
        handler!!.postDelayed({ dismissDialog() }, delayMillis)
    }
}
package com.hiskytech.userurdubolo.Ui

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.WindowManager
import android.webkit.DownloadListener
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.hiskytech.userurdubolo.R
import java.io.File

class ActivityWebView : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        supportActionBar?.hide()
        setContentView(R.layout.activity_web_view)
var link=intent.getStringExtra("link")!!
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        // Crucial for preventing Chrome from opening:
        webView.setWebViewClient(WebViewClient()) // Use WebViewClient instead of a custom one

        webView.setDownloadListener(DownloadListener { url, _, _, _, _ ->
            startDownload(url, true) // Saving to internal storage
        })
        val originalLink = link
        val modifiedLink = originalLink.replace("drive_link", "sharing")

        val directDownloadUrl = modifiedLink
        webView.loadUrl(directDownloadUrl)
    }
    fun getFileIdFromLink(link: String): String {
        val uri = Uri.parse(link)
        val pathSegments = uri.pathSegments
        return pathSegments[2] // The file ID is the third path segment
    }
    private fun startDownload(url: String, saveToInternalStorage: Boolean) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle("Downloading")
        request.setDescription("Downloading file...")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val destinationUri = if (saveToInternalStorage) {
            // Use getExternalFilesDir for internal storage downloads:
            val videosDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            Uri.fromFile(File(videosDir, "MyVideo.mp4"))
        } else {
            // Logic for external storage (if needed):
            val directory = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            request.setDestinationInExternalFilesDir(this, directory?.path, "MyVideo.mp4")
            null // Use null for external storage to let DownloadManager handle the path
        }

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        request.setDestinationUri(destinationUri)
        downloadManager.enqueue(request)
    }

}

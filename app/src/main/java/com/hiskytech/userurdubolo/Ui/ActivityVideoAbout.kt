package com.hiskytech.userurdubolo.Ui

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.hiskytech.userurdubolo.Model.ModelDrama
import com.hiskytech.userurdubolo.Model.ModelVideo
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.databinding.ActivityVideoAboutBinding

class ActivityVideoAbout : AppCompatActivity() {

    private lateinit var binding:ActivityVideoAboutBinding
    private var downloadId: Long = 0
private lateinit var modelVideo:ModelVideo


    private val downloadReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadId) {
                val filePath = getFilePath(context, downloadId)
                if (filePath.isNotEmpty()) {
                    Toast.makeText(context, "Video Downloaded!", Toast.LENGTH_SHORT).show()
                    // Perform actions after the video is downloaded if needed
                } else {
                    Toast.makeText(context, "Failed to download the video!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }





    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding=ActivityVideoAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val receivedIntent = intent
        modelVideo= ModelVideo()
        val modelDramaString = receivedIntent.getStringExtra("Video")
       modelVideo = ModelVideo.fromString(modelDramaString!!)!!
        Glide.with(this@ActivityVideoAbout).load(modelVideo?.thumbnail).centerCrop().into(binding.ivThumnail)
binding.ivplayer.setOnClickListener()
{
    val intent = Intent(this@ActivityVideoAbout, ActivityVideoPlayer::class.java)
    intent.putExtra("Video", modelVideo.toString())
    startActivity(intent)

}
        binding.ivback.setOnClickListener()
{
   finish()

}
        val description = "Watch the latest episode of ${modelVideo?.dramaName} , Episode ${modelVideo?.episodeno}. Dive into the gripping storyline and immerse yourself in the captivating world of UrduBolo. Enjoy the exciting narrative that will keep you on the edge of your seat."
        binding.tvDescription.text = description


        binding.floatingactiondownload.setOnClickListener {
            if (!isInternetAvailable()) {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            }
            else
            {
                startActivity(Intent(this@ActivityVideoAbout,ActivityWebView::class.java).putExtra("link",modelVideo.videourl))

            }
         /*   val googleDriveUrl = "https://drive.google.com/file/d/1A_db-ApDoaegYAivwKRk9EpjTPMKAhvn/view?usp=drive_link" // Replace this with your video URL
            val fileUrl = getDirectDownloadUrl(googleDriveUrl)!!
            downloadFile(this@ActivityVideoAbout, fileUrl)
            Toast.makeText(this@ActivityVideoAbout, modelVideo.thumbnail, Toast.LENGTH_SHORT).show()*/
        }
    }


    private fun downloadFile(context: Context, fileUrl: String) {
        val request = DownloadManager.Request(Uri.parse(fileUrl))
        request.setTitle("MyVideo")
        request.setDescription("Downloading video...")

        val url = modelVideo.thumbnail
        val uniqueIdentifier = extractUniqueIdentifier(url)

        if (uniqueIdentifier != null) {
            request.setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_DOWNLOADS,
                "C$uniqueIdentifier"
            )

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadId = downloadManager.enqueue(request)
        } else {
            // Handle the case where the unique identifier extraction fails
            // For example, display an error message or handle the download differently
            // This block will be executed if the URL format doesn't match the expected pattern
        }
    }

    fun extractUniqueIdentifier(url: String): String? {
        val startIndex = url.indexOf("thumbnails%2F") + "thumbnails%2F".length
        val endIndex = url.indexOf("?alt=media&token=")

        return if (startIndex >= 0 && endIndex > startIndex) {
            url.substring(startIndex, endIndex)
        } else {
            null // Return null if the URL format doesn't match
        }
    }
    private fun getDirectDownloadUrl(googleDriveUrl: String): String? {
        val fileId = googleDriveUrl.split("/d/")[1].substringBefore("/")
        return "https://drive.google.com/uc?export=download&id=$fileId"
    }

/*private fun getDirectDownloadUrl(googleDriveUrl: String): String {
    val fileId = googleDriveUrl.split("/d/")[1].substringBefore("/")
    return "https://drive.google.com/uc?export=download&id=$fileId"
}*/


    private fun registerDownloadReceiver() {
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(downloadReceiver, intentFilter)
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val networkInfo = connectivityManager?.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
    private fun getFilePath(context: Context, downloadId: Long): String {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val query = DownloadManager.Query().setFilterById(downloadId)

        val cursor = downloadManager.query(query)
        cursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val status = if (columnIndex != -1) cursor.getInt(columnIndex) else -1

                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    val uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                    val downloadedUri = if (uriIndex != -1) cursor.getString(uriIndex) else ""
                    val filePath = Uri.parse(downloadedUri).path ?: ""


                    return filePath
                }
            }
        }
        return ""
    }
}
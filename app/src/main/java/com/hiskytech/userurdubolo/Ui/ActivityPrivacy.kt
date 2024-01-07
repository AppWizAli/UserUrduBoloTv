package com.hiskytech.userurdubolo.Ui
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.hiskytech.userurdubolo.Adapter.VideoAdapter
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.databinding.ActivityPrivacyBinding
import java.io.File

class ActivityPrivacy : AppCompatActivity(), VideoAdapter.OnItemClickListener {
    private var player: SimpleExoPlayer? = null

    private lateinit var binding: ActivityPrivacyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val videoFiles = fetchDownloadedVideos()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewVideos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = VideoAdapter(this@ActivityPrivacy,videoFiles,this)
        recyclerView.adapter = adapter
        
        if(videoFiles.isEmpty())
            Toast.makeText(this@ActivityPrivacy, "EmptyList", Toast.LENGTH_SHORT).show()
    }
    private fun fetchDownloadedVideos(): ArrayList<String> {
        val videoFiles = ArrayList<String>()

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val files = storageDir?.listFiles { _, name -> name.startsWith("C") }

        files?.forEach { file ->
            videoFiles.add(file.absolutePath)
        }

        return videoFiles
    }



    /*private fun fetchDownloadedVideos(): ArrayList<String> {
        val videoFiles = ArrayList<String>()

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val files = storageDir?.listFiles { _, name -> name.startsWith("my_video") }

        files?.forEach { file ->
            videoFiles.add(file.absolutePath)
        }

        return videoFiles
    }*/

    override fun onItemClick(videoFile: String) {
initializePlayer(videoFile)
    }


    private fun initializePlayer(videoFilePath: String) {
        player = SimpleExoPlayer.Builder(this).build()
        binding.playerView.player = player

        val mediaItem = MediaItem.fromUri(videoFilePath)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }
}


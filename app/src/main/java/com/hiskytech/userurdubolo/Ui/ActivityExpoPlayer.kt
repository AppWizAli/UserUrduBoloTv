package com.hiskytech.userurdubolo.Ui
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.hiskytech.userurdubolo.databinding.ActivityExpoPlayerBinding

class ActivityExpoPlayer : AppCompatActivity() {
    private var player: SimpleExoPlayer? = null
    private lateinit var binding: ActivityExpoPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        supportActionBar?.hide()
        binding = ActivityExpoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoFilePath = intent.getStringExtra("videopath") ?: ""
        initializePlayer(videoFilePath)
    }

    private fun initializePlayer(videoFilePath: String) {
        player = SimpleExoPlayer.Builder(this).build()
        binding.playerView.player = player

        val mediaItem = MediaItem.fromUri(videoFilePath)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }
}

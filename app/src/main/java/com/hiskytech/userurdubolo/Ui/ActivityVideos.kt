package com.hiskytech.userurdubolo.Ui

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.WebView
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.admin.Utils
import com.hiskytech.userurdubolo.Adapter.AdapterAssignedVideo
import com.hiskytech.userurdubolo.Adapter.AdapterDrama
import com.hiskytech.userurdubolo.Adapter.AdapterSeason
import com.hiskytech.userurdubolo.Adapter.AdapteraddVideo
import com.hiskytech.userurdubolo.Data.SharedPrefManager
import com.hiskytech.userurdubolo.Model.ModelDrama
import com.hiskytech.userurdubolo.Model.ModelSeason
import com.hiskytech.userurdubolo.Model.ModelVideo
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.ViewModel.UserViewModel
import com.hiskytech.userurdubolo.databinding.ActivityVideoPlayerBinding
import com.hiskytech.userurdubolo.databinding.ActivityVideosBinding

class ActivityVideos : AppCompatActivity(),AdapteraddVideo.OnItemClickListener,AdapterSeason.OnItemClickListener
{
    private lateinit var binding: ActivityVideosBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var utils: Utils
    private lateinit var webView: WebView
    private var dialog: Dialog? = null
    private var handler: Handler? = null
    private lateinit var modelVideo: ModelVideo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding=ActivityVideosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        utils= Utils(this@ActivityVideos)
        sharedPrefManager= SharedPrefManager(this@ActivityVideos)
        modelVideo=ModelVideo()

        binding.rvVideos.layoutManager = GridLayoutManager(this@ActivityVideos,3)
        ///Drama
        val receivedIntent = intent
        val modelDramaString = receivedIntent.getStringExtra("Drama")
        val fromDramaString = receivedIntent.getStringExtra("from")
        val modelDrama = if (modelDramaString != null) {
            ModelDrama.fromString(modelDramaString)
        } else {
            ModelDrama()
        }
        val sortedList = sharedPrefManager.getPublicVideoList().filter { it.dramaId == modelDrama!!.docId }

        var sortedList2=sortedList.sortedBy { it.episodeno.toInt() }


        val receivedIntentseasons = intent
        val modelSeasonString = receivedIntentseasons.getStringExtra("Seasons")
        val fromSeasonString = receivedIntentseasons.getStringExtra("from")

        val modelSeason = modelSeasonString?.let { ModelSeason.fromString(it) }


        val SeasonsortedList = if (modelSeason != null) {
            sharedPrefManager.getPublicVideoList().filter { it.seasonId == modelSeason.docId }
        } else {
            listOf() // Provide a default value in case modelSeason is null
        }


if(fromSeasonString.equals("mostWatchedseasons"))
{

binding.tvBottomPlaceholder.visibility= View.GONE


binding.tvDramaName.text=modelSeason?.dramaName
    binding.rvVideos.adapter = AdapteraddVideo(this@ActivityVideos, SeasonsortedList, this@ActivityVideos)
}
if(fromDramaString.equals("OrignalDrama"))
{
    var seasonlist=sharedPrefManager.getSeasonList()
    var sortedSeasonslist=seasonlist.filter { it.dramaId==modelDrama?.docId }
    binding.rvSeasons.layoutManager=LinearLayoutManager(this@ActivityVideos,LinearLayoutManager.HORIZONTAL,false)
    binding.rvSeasons.adapter = AdapterSeason(this@ActivityVideos, sortedSeasonslist.sortedBy { it.seasonNo.toInt() }, this@ActivityVideos)
    binding.tvDramaName.text=modelDrama?.dramaName
    binding.rvVideos.adapter = AdapteraddVideo(this@ActivityVideos, sortedList2, this@ActivityVideos)
}




    }

    override fun onItemClick(modelDrama: ModelVideo) {
        startActivity(Intent(this@ActivityVideos,ActivityVideoAbout::class.java).putExtra("Video",modelDrama.toString()))
    }

    override fun onItemClick(modelSeason: ModelSeason) {
        binding.rvVideos.adapter = AdapteraddVideo(this@ActivityVideos, sharedPrefManager.getPublicVideoList().filter { it.seasonId==modelSeason.docId }.sortedBy { it.episodeno.toInt() }, this@ActivityVideos)
    }

    override fun onDeleteClick(modelSeason: ModelSeason) {

    }

    override fun onEditClick(modelSeason: ModelSeason) {

    }
}
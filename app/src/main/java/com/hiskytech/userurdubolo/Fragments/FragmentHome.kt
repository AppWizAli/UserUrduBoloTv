package com.hiskytech.userurdubolo.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.admin.Constants
import com.admin.Utils
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hiskytech.userurdubolo.Adapter.AdapterDrama
import com.hiskytech.userurdubolo.Adapter.AdapterSeason
import com.hiskytech.userurdubolo.Adapter.AdapteraddVideo
import com.hiskytech.userurdubolo.BannerItemFragment
import com.hiskytech.userurdubolo.Data.SharedPrefManager
import com.hiskytech.userurdubolo.Model.ModelDrama
import com.hiskytech.userurdubolo.Model.ModelSeason
import com.hiskytech.userurdubolo.Model.ModelUser
import com.hiskytech.userurdubolo.Model.ModelVideo
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.RecommendedViewPagerAdapter
import com.hiskytech.userurdubolo.Ui.ActivityVideoAbout
import com.hiskytech.userurdubolo.Ui.ActivityVideos
import com.hiskytech.userurdubolo.ViewModel.DramaViewModel
import com.hiskytech.userurdubolo.ViewModel.VideoViewModel
import com.hiskytech.userurdubolo.databinding.FragmentHomeBinding

class FragmentHome : Fragment() ,AdapterDrama.OnItemClickListener , AdapterSeason.OnItemClickListener,AdapteraddVideo.OnItemClickListener{


    private lateinit var viewPagerBanner: ViewPager2
    private val imageResources = listOf(
        R.drawable.b1,
        R.drawable.b2,
        R.drawable.b3,
        R.drawable.b4,
        R.drawable.b5
    )
    private lateinit var bannerAdapter: BannerAdapter


    private var isPageScrolling = false








    private val recommendedImages = listOf(
        R.drawable.img_8,
        R.drawable.img_9,
        R.drawable.img_10,
        R.drawable.img_11,
        // Add more images here if needed
    )

    private lateinit var viewPagerRecommended: ViewPager2
    private lateinit var recommendedAdapter: RecomendedAdapter



    private var _binding: FragmentHomeBinding? = null

    private val videoViewModel: VideoViewModel by viewModels()
    private val dramaViewModel: DramaViewModel by viewModels()
    private val db = Firebase.firestore
    private var imageURI: Uri? = null
    private val IMAGE_PICKER_REQUEST_CODE = 200
    private var deleteDialog: AlertDialog? = null
    private lateinit var modelDrama: ModelDrama

    private lateinit var thumnailview: ImageView
    private lateinit var adapter: AdapterDrama

    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var constants: Constants
    private lateinit var user: ModelUser
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var dialog: Dialog
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        mContext = requireContext()
        utils = Utils(mContext)
        modelDrama = ModelDrama()
        var modellist = ArrayList<ModelDrama>()
        constants = Constants()

        sharedPrefManager=SharedPrefManager(mContext)




        viewPagerBanner = binding.viewPagerBanner
        bannerAdapter = BannerAdapter(imageResources)
        viewPagerBanner.adapter = bannerAdapter

        viewPagerRecommended = binding.viewPagerRecommended
        recommendedAdapter = RecomendedAdapter(recommendedImages)
        viewPagerRecommended.adapter = recommendedAdapter

        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                val currentItem = viewPagerBanner.currentItem
                val maxItems = bannerAdapter.itemCount

                // If it's the last item, smoothly transition to the first item without any delay
                if (currentItem == maxItems - 1) {
                    viewPagerBanner.setCurrentItem(0, false)
                } else {
                    viewPagerBanner.setCurrentItem(currentItem + 1, true)
                }

                handler.postDelayed(this, 3000)
            }
        }

// Start the initial runnable
        handler.postDelayed(runnable, 3000)

        viewPagerBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                isPageScrolling = state == ViewPager2.SCROLL_STATE_DRAGGING || state == ViewPager2.SCROLL_STATE_SETTLING
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    handler.postDelayed(runnable, 3000)
                } else {
                    handler.removeCallbacks(runnable)
                }
            }
        })
        val handler2 = Handler()
        val runnable2 = object : Runnable {
            override fun run() {
                val currentItem = viewPagerRecommended.currentItem
                val maxItems = recommendedAdapter.itemCount

                if (currentItem == maxItems - 1) {
                    viewPagerRecommended.setCurrentItem(0, false)
                } else {
                    viewPagerRecommended.setCurrentItem(currentItem + 1, true)
                }

                handler2.postDelayed(this, 3000)
            }
        }

// Start the initial runnable
        handler2.postDelayed(runnable2, 3000)

        viewPagerRecommended.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                isPageScrolling = state == ViewPager2.SCROLL_STATE_DRAGGING || state == ViewPager2.SCROLL_STATE_SETTLING
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    handler2.postDelayed(runnable2, 3000)
                } else {
                    handler2.removeCallbacks(runnable2)
                }
            }
        })

        ////












/*if(sharedPrefManager.getDramaList().size==0)
{
    Toast.makeText(mContext, "Zero", Toast.LENGTH_SHORT).show()
}*/

//done with all dramas
        binding.rvOriginalSeries.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        binding.rvOriginalSeries.adapter = AdapterDrama(mContext, sharedPrefManager.getDramaList(), this@FragmentHome)

//done with all Seasons
        binding.rvMostWatchSeries.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        binding.rvMostWatchSeries.adapter = AdapterSeason(mContext, sharedPrefManager.getSeasonList(), this@FragmentHome)




///done witrh hudustuz video list
        binding.rvHudusuz.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHudusuz.adapter = AdapteraddVideo(mContext, sharedPrefManager.getHudutsuzVideoList(), this@FragmentHome)



///done withSeasons of Cukur
        binding.rvCukur.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
       val CukurList= sharedPrefManager.getSeasonList().filter { it.dramaName == "Çukur " }
        binding.rvCukur.adapter = AdapterSeason(mContext, CukurList.sortedBy { it.seasonNo }, this@FragmentHome)




///done with FanFavoriteAllVideos video list
        binding.rvFanFavorites.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        binding.rvFanFavorites.adapter = AdapteraddVideo(mContext, sharedPrefManager.getPublicVideoList().take(10).sortedBy { it.episodeno }, this@FragmentHome)


///done with TopratedSeasons soted by uplaoded at
        binding.rvtopRatedDramas.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        val TopRatedSeasonList = sharedPrefManager.getSeasonList().sortedByDescending { it.uploadedAt }.take(15)

        binding.rvtopRatedDramas.adapter = AdapterSeason(mContext, TopRatedSeasonList, this@FragmentHome)


///done with  video list only 15 items
        binding.rvRecomended.layoutManager = GridLayoutManager(mContext,3)
        binding.rvRecomended.adapter = AdapteraddVideo(mContext, sharedPrefManager.getPublicVideoList().sortedByDescending { it.uploadedAt }.take(6), this@FragmentHome)


        binding.rvFilm.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        val FilmList = sharedPrefManager.getPublicVideoList().filter { it.dramaName=="Turkish Dundrum film" }
        binding.rvFilm.adapter = AdapteraddVideo(mContext, FilmList, this@FragmentHome)


///done with  video list only 15 items


// Assuming sharedPrefManager.getPublicVideoList() returns a list of videos
        val videoList = sharedPrefManager.getPublicVideoList()

// Calculate the size of each part (assuming the list can be divided evenly into three parts)
        val partSize = videoList.size / 3

// Split the videoList into three parts
        val part1 = videoList.subList(0, partSize)
        val part2 = videoList.subList(partSize, 2 * partSize)
        val part3 = videoList.subList(2 * partSize, videoList.size)

// Set each part to a different RecyclerView
        binding.rvUnveil.layoutManager = GridLayoutManager(mContext, 30, GridLayoutManager.HORIZONTAL, false)
        binding.rvUnveil.adapter = AdapteraddVideo(mContext, part1.sortedBy { it.episodeno.toInt() }, this@FragmentHome)

// Assuming you have three RecyclerViews named rvFirstPart, rvSecondPart, and rvThirdPart
        binding.rvEnveil2.layoutManager =GridLayoutManager(mContext, 30, GridLayoutManager.HORIZONTAL, false)
        binding.rvEnveil2.adapter = AdapteraddVideo(mContext, part2.sortedBy { it.episodeno.toInt() }, this@FragmentHome)

        binding.rvUnveil3.layoutManager = GridLayoutManager(mContext, 30, GridLayoutManager.HORIZONTAL, false)
        binding.rvUnveil3.adapter = AdapteraddVideo(mContext, part3.sortedBy { it.episodeno.toInt() }, this@FragmentHome)






        return root
    }

    override fun onItemClick(modelDrama: ModelDrama) {
        val intent = Intent(mContext, ActivityVideos::class.java).apply {
            putExtra("Drama", modelDrama.toString())
            putExtra("from", "OrignalDrama")
        }
        startActivity(intent)


    }

    override fun onDeleteClick(modelDrama: ModelDrama) {

    }

    override fun onEditClick(modelDrama: ModelDrama) {
  
    }

    override fun onItemClick(modelSeason: ModelSeason) {
        val intent = Intent(mContext, ActivityVideos::class.java).apply {
            putExtra("Seasons", modelSeason?.toString())
            putExtra("from", "mostWatchedseasons")
        }
        startActivity(intent)
    }

    override fun onDeleteClick(modelSeason: ModelSeason) {

    }

    override fun onEditClick(modelSeason: ModelSeason) {

    }

    override fun onItemClick(modelDrama: ModelVideo) {
        startActivity(Intent(requireContext(),ActivityVideoAbout::class.java).putExtra("Video",modelDrama.toString()))
    }

    inner class BannerAdapter(private val images: List<Int>) :
        FragmentStateAdapter(requireActivity()) {

        override fun getItemCount(): Int = images.size

        override fun createFragment(position: Int): Fragment {
            val fragment = BannerItemFragment()
            fragment.arguments = Bundle().apply {
                putInt(BannerItemFragment.ARG_IMAGE_RESOURCE, images[position])
            }
            return fragment
        }
    }
    inner class RecomendedAdapter(private val images: List<Int>) :
        FragmentStateAdapter(requireActivity()) {

        override fun getItemCount(): Int = images.size

        override fun createFragment(position: Int): Fragment {
            val fragment = RecommendedViewPagerAdapter()
            fragment.arguments = Bundle().apply {
                putInt(RecommendedViewPagerAdapter.ARG_IMAGE_RESOURCE, images[position])
            }
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
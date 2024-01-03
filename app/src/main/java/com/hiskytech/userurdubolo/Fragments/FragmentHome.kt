package com.hiskytech.userurdubolo.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.admin.Constants
import com.admin.Utils
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hiskytech.userurdubolo.Adapter.AdapterDrama
import com.hiskytech.userurdubolo.Adapter.AdapterSeason
import com.hiskytech.userurdubolo.Data.SharedPrefManager
import com.hiskytech.userurdubolo.Model.ModelDrama
import com.hiskytech.userurdubolo.Model.ModelSeason
import com.hiskytech.userurdubolo.Model.ModelUser
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.Ui.ActivityVideoAbout
import com.hiskytech.userurdubolo.ViewModel.DramaViewModel
import com.hiskytech.userurdubolo.ViewModel.VideoViewModel
import com.hiskytech.userurdubolo.databinding.FragmentHomeBinding

class FragmentHome : Fragment() ,AdapterDrama.OnItemClickListener , AdapterSeason.OnItemClickListener{

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
/*if(sharedPrefManager.getDramaList().size==0)
{
    Toast.makeText(mContext, "Zero", Toast.LENGTH_SHORT).show()
}*/
        binding.rvMostWatchSeries.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        binding.rvOriginalSeries.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        binding.rvOriginalSeries.adapter = AdapterDrama(mContext, sharedPrefManager.getDramaList(), this@FragmentHome)
        binding.rvCukur.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCukur.adapter = AdapterDrama(mContext, sharedPrefManager.getDramaList(), this@FragmentHome)
        binding.rvMostWatchSeries.adapter = AdapterDrama(mContext, sharedPrefManager.getDramaList(), this@FragmentHome)
        binding.rvHudusuz.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)


            binding.rvHudusuz.adapter = AdapterSeason(mContext, sharedPrefManager.getHudutsuzSeasonList(), this@FragmentHome)

        
        return root
    }

    override fun onItemClick(modelDrama: ModelDrama) {
        startActivity(Intent(mContext, ActivityVideoAbout::class.java).apply {
            putExtra("Drama", modelDrama.toString())
        })


    }

    override fun onDeleteClick(modelDrama: ModelDrama) {

    }

    override fun onEditClick(modelDrama: ModelDrama) {
  
    }

    override fun onItemClick(modelSeason: ModelSeason) {

    }

    override fun onDeleteClick(modelSeason: ModelSeason) {

    }

    override fun onEditClick(modelSeason: ModelSeason) {

    }


}
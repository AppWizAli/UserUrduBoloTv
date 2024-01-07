package com.hiskytech.userurdubolo.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.IInterface
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.ActivityNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.admin.Constants
import com.admin.Utils
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hiskytech.userurdubolo.Adapter.AdapterDrama
import com.hiskytech.userurdubolo.Adapter.VideoAdapter
import com.hiskytech.userurdubolo.Data.SharedPrefManager
import com.hiskytech.userurdubolo.Model.ModelDrama
import com.hiskytech.userurdubolo.Model.ModelUser
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.Ui.ActivityExpoPlayer
import com.hiskytech.userurdubolo.ViewModel.DramaViewModel
import com.hiskytech.userurdubolo.ViewModel.VideoViewModel
import com.hiskytech.userurdubolo.databinding.FragmentHomeBinding
import com.hiskytech.userurdubolo.databinding.FragmentSearchBinding


class FragmentSearch : Fragment(),VideoAdapter.OnItemClickListener {
    private var _binding: FragmentSearchBinding? = null

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
    private var player: SimpleExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()

        return root
    }

    private fun setupRecyclerView() {
        val videoFiles = fetchDownloadedVideos()

        binding.rvDownloadEpisodes.layoutManager = GridLayoutManager(requireContext(), 3)

        if (videoFiles.isEmpty()) {
            Toast.makeText(requireContext(), "Nothing to Show, Download Video First", Toast.LENGTH_SHORT).show()
        } else {
            binding.rvDownloadEpisodes.adapter = VideoAdapter(requireContext(), videoFiles, this@FragmentSearch)
        }
    }
    private fun fetchDownloadedVideos(): ArrayList<String> {
        val videoFiles = ArrayList<String>()

        // Access the correct download directory:
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        val files = storageDir?.listFiles() // List all files in the directory

        files?.forEach { file ->
            if (file.name.startsWith("MyVideo")) { // Filter for the specific filename
                videoFiles.add(file.absolutePath)
            }
        }

        return videoFiles
    }

    override fun onItemClick(videoFile: String) {
        startActivity(Intent(requireContext(), ActivityExpoPlayer::class.java).putExtra("videopath",videoFile))
    }

}
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.admin.Constants
import com.admin.Utils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hiskytech.userurdubolo.Adapter.AdapterDrama
import com.hiskytech.userurdubolo.Adapter.AdapteraddVideo
import com.hiskytech.userurdubolo.Data.SharedPrefManager
import com.hiskytech.userurdubolo.Model.ModelDrama
import com.hiskytech.userurdubolo.Model.ModelGroup
import com.hiskytech.userurdubolo.Model.ModelUser
import com.hiskytech.userurdubolo.Model.ModelVideo
import com.hiskytech.userurdubolo.Model.ModelVideoManagment
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.Ui.ActivityVideoAbout
import com.hiskytech.userurdubolo.ViewModel.DramaViewModel
import com.hiskytech.userurdubolo.ViewModel.VideoViewModel
import com.hiskytech.userurdubolo.databinding.FragmentHomeBinding
import com.hiskytech.userurdubolo.databinding.FragmentPrivateBinding


class FragmentPrivate : Fragment() ,AdapteraddVideo.OnItemClickListener {
    private var _binding: FragmentPrivateBinding? = null

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

        _binding = FragmentPrivateBinding.inflate(inflater, container, false)
        val root: View = binding.root


        mContext = requireContext()
        utils = Utils(mContext)
        modelDrama = ModelDrama()
        var modellist = ArrayList<ModelDrama>()
        constants = Constants()

        sharedPrefManager = SharedPrefManager(mContext)
binding.whatsapp.setOnClickListener()
{
        val message =
            "🎬 Hi there! I'm enjoying the awesome content on your video streaming app! Got a question or suggestion to make it even better? Let's chat! 📺🌟" // Customized message with emojis

        val uri =
            Uri.parse("https://api.whatsapp.com/send?phone=923007392932 &text=${Uri.encode(message)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)

}

        binding.rvPrivateVideos.layoutManager =
            GridLayoutManager(mContext, 3)


        // Initialize Firestore instance
        val db = FirebaseFirestore.getInstance()

// Placeholder for the user's docId
        val userDocId = sharedPrefManager.getUser().userId

// List to hold the resulting ModelVideo
        val modelVideoList: MutableList<ModelVideo> = mutableListOf()

// Step 1: Query GroupsCollection to find documents where userDocId is present in the 'users' list
        db.collection("Groups")
            .whereArrayContains("users", userDocId)
            .get()
            .addOnSuccessListener { groupDocuments ->
                utils.startLoadingAnimation()
                val groupIdList: MutableList<String> = mutableListOf()

                // Step 2: Get the list of groupIds from the retrieved documents
                for (groupDocument in groupDocuments) {
                    val modelGroup = groupDocument.toObject(ModelGroup::class.java)
                    groupIdList.add(modelGroup.doc_Id)
                }

// Check if groupIdList is not empty before querying ManageVideo
                if (groupIdList.isNotEmpty()) {
                    // Step 3: Query ManageVideo to find documents where groupIdList matches in 'modelVideoManagement.groupId'
                    db.collection("ManageVideo")
                        .whereIn("group_Id", groupIdList)
                        .get()
                        .addOnSuccessListener { videoManagementDocuments ->
                            val videoIdList: MutableList<String> = mutableListOf()

                            // Step 4: Get the list of videoIds from the retrieved documents
                            for (videoManagementDocument in videoManagementDocuments) {
                                val modelVideoManagement =
                                    videoManagementDocument.toObject(ModelVideoManagment::class.java)
                                videoIdList.add(modelVideoManagement.video_Id)
                            }

                            // Check if videoIdList is not empty before querying Videos
                            if (videoIdList.isNotEmpty()) {
                                // Step 5: Query Videos to find documents where videoIdList matches in 'docId'
                                db.collection("Videos")
                                    .whereIn("docId", videoIdList)
                                    .get()
                                    .addOnSuccessListener { videoDocuments ->
                                        utils.endLoadingAnimation()
                                        // Step 6: Get the ModelVideo documents
                                        for (videoDocument in videoDocuments) {
                                            val modelVideo =
                                                videoDocument.toObject(ModelVideo::class.java)
                                            modelVideoList.add(modelVideo)
                                        }


                                        binding.rvPrivateVideos.adapter =
                                            AdapteraddVideo(
                                                mContext,
                                                modelVideoList,
                                                this@FragmentPrivate
                                            )
                                        // At this point, modelVideoList contains the required ModelVideo documents
                                        // Use the modelVideoList as needed
                                    }
                                    .addOnFailureListener { exception ->
                                        utils.endLoadingAnimation()
                                    }
                            } else {
                                utils.endLoadingAnimation()
                                binding.tvPrivate.visibility = View.VISIBLE
                            }
                        }
                        .addOnFailureListener { exception ->
                            utils.endLoadingAnimation()  // Handle failure
                        }
                } else {
                    utils.endLoadingAnimation()
                    binding.tvPrivate.visibility = View.VISIBLE
                }


            }







                return  root
    }

    override fun onItemClick(modelDrama: ModelVideo) {
    startActivity(Intent(mContext,ActivityVideoAbout::class.java).putExtra("Video",modelDrama.toString()))
    }


}
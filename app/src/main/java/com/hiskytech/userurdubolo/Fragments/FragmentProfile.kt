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
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.admin.Constants
import com.admin.Utils
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hiskytech.userurdubolo.Data.SharedPrefManager
import com.hiskytech.userurdubolo.Model.ModelUser
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.Ui.ActivityAbout
import com.hiskytech.userurdubolo.Ui.ActivityEditProfile
import com.hiskytech.userurdubolo.Ui.ActivityIntro
import com.hiskytech.userurdubolo.Ui.ActivityPrivacy
import com.hiskytech.userurdubolo.Ui.MainActivity
import com.hiskytech.userurdubolo.ViewModel.UserViewModel
import com.hiskytech.userurdubolo.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch


class FragmentProfile : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    private val userViewModel: UserViewModel by viewModels()


    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var constants: Constants
    private lateinit var user: ModelUser
    private lateinit var sharedPrefManager: SharedPrefManager

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPrefManager = SharedPrefManager(requireContext())

        mContext = requireContext()
        utils = Utils(mContext)

        user = ModelUser()


if(sharedPrefManager.getUser().name.isBlank())
{
    binding.layAlert.visibility=View.VISIBLE
    binding.layProfile.visibility=View.GONE
}

        binding.name.text = sharedPrefManager.getUser().name

        Glide.with(requireContext()).load(sharedPrefManager.getUser().profile).centerCrop().placeholder(R.drawable.img_7).error(R.drawable.img_7).into(binding.crd2)
        binding.terms.setOnClickListener()
        {
            Toast.makeText(
                mContext,
                "Available Soon! UnderWorking due to schedule maintinance",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.about.setOnClickListener()
        {

           startActivity(Intent(requireContext(),ActivityAbout::class.java))
        }
        binding.editprofile.setOnClickListener()
        {
            startActivity(Intent(mContext, ActivityEditProfile::class.java))
        }


        binding.call.setOnClickListener()
        {
            openDialer("03007392932")
        }
        binding.message.setOnClickListener()
        {
            openWhatsApp("923007392932")
        }
        binding.logout.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("Logout!!")
            alertDialogBuilder.setMessage("Are you sure you want to logout?")
            alertDialogBuilder.setPositiveButton("Logout") { dialog, which ->
                lifecycleScope.launch {
                    var user = sharedPrefManager.getUser()
                    user.loggedInNumber = ""
                    userViewModel.updateUser(user!!).observe(viewLifecycleOwner) { task ->
                        if (task) {
                            startActivity(Intent(requireContext(), ActivityIntro::class.java))
                            sharedPrefManager.clearWholeSharedPrefrences()
                            requireActivity().finish()
                        } else {
                            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }


        return root

    }


    private fun openWhatsApp(phone: String) {
        val message =
            "🎬 Hi there! I'm enjoying the awesome content on your video streaming app! Got a question or suggestion to make it even better? Let's chat! 📺🌟" // Customized message with emojis

        val uri =
            Uri.parse("https://api.whatsapp.com/send?phone=$phone&text=${Uri.encode(message)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }


    private fun openDialer(phoneNumber: String) {
        val uri = Uri.parse("tel:$phoneNumber")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        startActivity(intent)
    }


}
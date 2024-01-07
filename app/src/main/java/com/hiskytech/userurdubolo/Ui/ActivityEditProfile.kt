package com.hiskytech.userurdubolo.Ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.admin.Utils
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hiskytech.userurdubolo.Data.SharedPrefManager
import com.hiskytech.userurdubolo.Model.ModelUser
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ActivityEditProfile : AppCompatActivity() {

    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var imageUri: Uri
    private lateinit var utils:Utils
    private lateinit var storageReference: StorageReference

    companion object {
        private const val IMAGE_PICKER_REQUEST_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityEditProfile
        sharedPrefManager = SharedPrefManager(mContext)
        storageReference = FirebaseStorage.getInstance().reference
utils= Utils(this@ActivityEditProfile)
        edit()

        binding.btnsignup.setOnClickListener {
            update()
        }

        binding.editPIC.setOnClickListener {
            openGalleryForImage()
        }
    }

    private fun edit() {
        val model = sharedPrefManager.getUser()
        if (model != null) {
            Glide.with(mContext)
                .load(model.profile)
                .centerCrop()
                .placeholder(R.drawable.icon1)
                .into(binding.imgView)
            binding.etname.setText(model.name)
            binding.etemail.setText(model.email)
            binding.etPassword.setText(model.password)
        }
    }

    private fun update() {
        utils.startLoadingAnimation()
        val user = sharedPrefManager.getUser()

        // Update user object with the new name, email, and password
        user.name = binding.etname.text.toString()
        user.email = binding.etemail.text.toString()
        user.password = binding.etPassword.text.toString()

        // Start the process to upload the image to Firebase Storage
        if (::imageUri.isInitialized) {
            lifecycleScope.launch(Dispatchers.IO) {
                uploadImageToFirebaseStorage(user) // Pass the user object
            }
        } else {
            Toast.makeText(mContext, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }


    private suspend fun uploadImageToFirebaseStorage(userId: ModelUser) {
        try {
            val imageRef = storageReference.child("profile_images/${userId.userId}.jpg")

            // Convert the selected image URI to InputStream and read its bytes
            val imageStream = contentResolver.openInputStream(imageUri)
            val imageData = imageStream?.readBytes()

            imageData?.let {
                val uploadTask = imageRef.putBytes(it).await()

                if (uploadTask.task.isSuccessful) {
                    val downloadUrl = imageRef.downloadUrl.await().toString()
                    userId.profile = downloadUrl

                    // Update the user profile in Firestore
                    updateProfileInFirestore(userId)
                } else {
                    runOnUiThread {
                        Toast.makeText(mContext, "Failed to upload image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            runOnUiThread {
                Toast.makeText(mContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateProfileInFirestore(user: ModelUser) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").document(user.userId)
            .set(user)
            .addOnSuccessListener {
                utils.endLoadingAnimation()
                Toast.makeText(mContext, "Profile Updated", Toast.LENGTH_SHORT).show()
                sharedPrefManager.saveUser(user)
                finish()
            }
            .addOnFailureListener { e ->
                utils.endLoadingAnimation()
                Toast.makeText(mContext, "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICKER_REQUEST_CODE) {
            data?.data?.let { uri ->
                imageUri = uri
                Glide.with(mContext)
                    .load(uri)
                    .centerCrop()
                    .placeholder(R.drawable.icon1)
                    .into(binding.imgView)
            }
        }
    }
}

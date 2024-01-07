package com.hiskytech.userurdubolo.Ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.admin.Constants
import com.admin.Utils
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hiskytech.userurdubolo.Data.SharedPrefManager
import com.hiskytech.userurdubolo.Model.ModelUser
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.ViewModel.UserViewModel
import com.hiskytech.userurdubolo.databinding.ActivitySignUpBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ActivitySignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var constants: Constants
    private lateinit var user: ModelUser
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var createAccountText: TextView
    private lateinit var storageReference: StorageReference
    private lateinit var uriString: Uri
    private lateinit var iconTransparent: ImageView
    private val colors = listOf("#FEC10F", "#FFFFFFFF", "#F1054E")
    private val IMAGE_PICKER_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivitySignUp
        utils = Utils(mContext)
        constants = Constants()
        user = ModelUser()
        storageReference = FirebaseStorage.getInstance().reference
        sharedPrefManager = SharedPrefManager(mContext)
        createAccountText = findViewById(R.id.createAccountText)
        iconTransparent = findViewById(R.id.icon_transparent)
        binding.tvSelect.setOnClickListener {
            openGalleryForImage()
        }
        uriString = "".toUri()
        binding.btnsignup.setOnClickListener()
        {
            val name = binding.etname.text.toString().trim()
            val email = binding.etemail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etCPassword.text.toString().trim()
            if (uriString.toString().isBlank()) {
                Toast.makeText(mContext, "Please Select Image!!", Toast.LENGTH_SHORT).show()
            }
            else if (name.isEmpty()) {
                showToast("Please enter your name.")

            }
            else  if (!isValidEmail(email)) {
                showToast("Please enter a valid email address.")

            }
            else  if (password.isEmpty()) {
                showToast("Please enter a password.")

            }
            else  if (password.length < 6) {
                showToast("Password should be at least 6 characters long.")

            }
            else  if (confirmPassword.isEmpty()) {
                showToast("Please confirm your password.")

            }
            else  if(password != confirmPassword) {
                showToast("Passwords do not match.")

            }
            else  if(uriString.toString().isBlank()) {
                showToast("Please Select Image")

            } else {

                user.name = name
                user.email = email
                user.password = password


                uploadImageToFirebaseStorage(uriString)
            }

        }
        binding.tvLogin.setOnClickListener()
        {
            startActivity(Intent(this@ActivitySignUp, ActivityLogin::class.java))
        }
        val customFont = ResourcesCompat.getFont(this, R.font.myfont)
        createAccountText.typeface = customFont

        // Start the text animation
        animateTextWithDifferentColors(
            "Welcome to UrduBolo Tv! Create an account to explore exclusive content......",
            createAccountText,
            100
        )
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                binding.image.setImageURI(uri)
                uriString = uri
            }
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri) {
        utils.startLoadingAnimation()
        val imageRef = storageReference.child("profile_images/${System.currentTimeMillis()}.jpg")

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val uploadTask = imageRef.putFile(imageUri).await()
                if (uploadTask.task.isSuccessful) {
                    val downloadUrl = imageRef.downloadUrl.await().toString()
                    user.profile = downloadUrl  // Set the user profile URL here

                    launch(Dispatchers.Main) {
                        utils.endLoadingAnimation()
                        showToast("Image uploaded successfully")
                        addUser(user)
                    }
                } else {
                    launch(Dispatchers.Main) {
                        utils.endLoadingAnimation()
                        showToast("Failed to upload image")
                    }
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    utils.endLoadingAnimation()
                    showToast("Error: ${e.message}")
                }
            }
        }
    }


// Rest of your code remains the same


    private fun animateTextWithDifferentColors(text: String, textView: TextView, delay: Long) {
        val handler = Handler()
        var i = 0

        handler.postDelayed(object : Runnable {
            override fun run() {
                if (i < text.length) {
                    val coloredText = getColoredText(text.substring(0, i + 1), i % colors.size)
                    textView.text = coloredText
                    i++
                    handler.postDelayed(
                        this,
                        100
                    ) // Change speed here (100ms for letter transition)
                } else {
                    // Text animation ended, start the image animation here
                    startImageAnimation()
                }
            }
        }, delay)
    }

    private fun getColoredText(text: String, colorIndex: Int): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        for (i in text.indices) {
            val color = Color.parseColor(colors[colorIndex])
            val spanText = SpannableStringBuilder(text[i].toString())
            spanText.setSpan(ForegroundColorSpan(color), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            builder.append(spanText)
        }
        return builder
    }

    private fun startImageAnimation() {
        // Start the infinite fade in and out animation for the ImageView
        val fadeInOut = AlphaAnimation(1f, 0.2f)
        fadeInOut.duration = 1000 // Set the duration of the animation in milliseconds
        fadeInOut.repeatCount = Animation.INFINITE // Set to repeat infinitely
        fadeInOut.repeatMode = Animation.REVERSE // Reverse the animation when repeating
        iconTransparent.startAnimation(fadeInOut)
    }

    private fun signUp() {


    }

    private fun addUser(modelUser: ModelUser) {

        lifecycleScope.launch {
            // Call the function to add user details to Firestore
            userViewModel.addUser(modelUser).observe(this@ActivitySignUp) { task ->
                if (task) {
                    utils.endLoadingAnimation()
                    showToast("Sign Up Successful!")
                    startActivity(Intent(this@ActivitySignUp, ActivityLogin::class.java))
                    finish()
                } else {
                    utils.endLoadingAnimation()
                    Toast.makeText(
                        mContext,
                        constants.SOMETHING_WENT_WRONG_MESSAGE,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showToast(message: String) {
        Toast.makeText(this@ActivitySignUp, message, Toast.LENGTH_SHORT).show()
    }

}


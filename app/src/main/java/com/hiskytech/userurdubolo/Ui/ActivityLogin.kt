package com.hiskytech.userurdubolo.Ui

import com.hiskytech.userurdubolo.Ui.ActivitySignUp
import com.hiskytech.userurdubolo.Ui.MainActivity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.admin.Utils
import com.google.firebase.firestore.QuerySnapshot
import com.hiskytech.userurdubolo.Data.SharedPrefManager
import com.hiskytech.userurdubolo.Model.ModelUser
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.ViewModel.UserViewModel
import com.hiskytech.userurdubolo.databinding.ActivityLoginBinding
import com.hiskytech.userurdubolo.databinding.ActivitySignUpBinding
import kotlinx.coroutines.launch

class ActivityLogin : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var dialog: Dialog
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var utils: Utils
    private lateinit var createAccountText: TextView
    private lateinit var iconTransparent: ImageView
    private val colors = listOf("#FEC10F", "#FFFFFFFF", "#F1054E")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        utils= Utils(this@ActivityLogin)
        sharedPrefManager= SharedPrefManager(this@ActivityLogin)
        binding.btnsignin.setOnClickListener()
        {
           // startActivity(Intent(this@ActivityLogin,MainActivity::class.java))
            signIn()
        }
        binding.tvSignuP.setOnClickListener()
        {
            startActivity(Intent(this@ActivityLogin, ActivitySignUp::class.java))
        }

        createAccountText = findViewById(R.id.createAccountText)
        iconTransparent = findViewById(R.id.icon_transparent)

        val customFont = ResourcesCompat.getFont(this, R.font.myfont)
        createAccountText.typeface = customFont

        // Start the text animation
        animateTextWithDifferentColors(
            "Welcome to UrduBolo Tv! Login to explore exclusive content......",
            createAccountText,
            100
        )
    }

    private fun animateTextWithDifferentColors(text: String, textView: TextView, delay: Long) {
        val handler = Handler()
        var i = 0

        handler.postDelayed(object : Runnable {
            override fun run() {
                if (i < text.length) {
                    val coloredText = getColoredText(text.substring(0, i + 1), i % colors.size)
                    textView.text = coloredText
                    i++
                    handler.postDelayed(this, 100) // Change speed here (100ms for letter transition)
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


    private fun signIn() {
        val email = binding.etemail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            showToast("Please enter your email.")
            return
        }

        if (!isValidEmail(email)) {
            showToast("Please enter a valid email address.")
            return
        }

        if (TextUtils.isEmpty(password)) {
            showToast("Please enter your password.")
            return
        }

        getUserEmailFromFirestore()

    }
    private fun getUserEmailFromFirestore() {
        lifecycleScope.launch {
            utils.startLoadingAnimation()
            userViewModel.getUser(binding.etPassword.text.toString() ,binding.etemail.text.toString()).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    utils.endLoadingAnimation()
                    val querySnapshot: QuerySnapshot? = task.result
                    if (querySnapshot != null && !querySnapshot.isEmpty) {
                        val user: ModelUser? = querySnapshot.documents[0].toObject(ModelUser::class.java)!!

                        if(user?.loggedInNumber.equals("1"))
                        {

                            showToast("This account is already loggedIn on any other device!Logout first it then retry!")
                        }
                        else
                        {
                            user?.loggedInNumber="1"
                            lifecycleScope.launch {
                                userViewModel.updateUser(user!!).observe(this@ActivityLogin)
                                {
                                    task->
                                    if(task)
                                    {
                                        showToast("Sign In Successful!")
                                        startActivity(Intent(this@ActivityLogin, MainActivity::class.java))
                                        sharedPrefManager.saveUserLogin(true)
                                        sharedPrefManager.saveUser(user)
                                    }
                                    else
                                    {
                                        showToast("Something went wrong!!")
                                    }
                                }

                            }

                        }

                    } else {
                        utils.endLoadingAnimation()
                       showToast("Invalid Credentials")
                    }
                } else {
                    utils.endLoadingAnimation()
                    showToast("Invalid Credentials")
                }
            }
        }
    }
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showToast(message: String) {
        Toast.makeText(this@ActivityLogin, message, Toast.LENGTH_SHORT).show()
    }
}
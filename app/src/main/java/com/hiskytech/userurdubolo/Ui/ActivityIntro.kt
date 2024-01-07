package com.hiskytech.userurdubolo.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hiskytech.userurdubolo.databinding.ActivityIntroBinding
import com.hiskytech.userurdubolo.databinding.ActivitySignUpBinding

class ActivityIntro : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnGuest.setOnClickListener()
        {
            startActivity(Intent(this@ActivityIntro, MainActivity::class.java))
        }
        binding.btnSignIn.setOnClickListener()
        {
            startActivity(Intent(this@ActivityIntro, ActivityLogin::class.java))
        }
        binding.tvSignUp.setOnClickListener()
        {
            startActivity(Intent(this@ActivityIntro, ActivitySignUp::class.java))
        }
        binding.tvterms.setOnClickListener()
        {
            Toast.makeText(this@ActivityIntro, "Available Soon!", Toast.LENGTH_SHORT).show()
        }
        binding.tvPrivacy.setOnClickListener()
        {
            Toast.makeText(this@ActivityIntro, "Available Soon!", Toast.LENGTH_SHORT).show()
        }

    }
}
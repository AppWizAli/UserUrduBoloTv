package com.hiskytech.userurdubolo.Ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.hiskytech.userurdubolo.Model.ModelDrama
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.databinding.ActivityVideoAboutBinding

class ActivityVideoAbout : AppCompatActivity() {

    private lateinit var binding:ActivityVideoAboutBinding
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding=ActivityVideoAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val receivedIntent = intent
        val modelDramaString = receivedIntent.getStringExtra("Drama")
        val modelDrama = ModelDrama.fromString(modelDramaString!!)
        Toast.makeText(this@ActivityVideoAbout, modelDrama?.dramaName, Toast.LENGTH_SHORT).show()
        Glide.with(this@ActivityVideoAbout).load(modelDrama?.thumbnail).centerCrop().placeholder(R.drawable.ic_launcher_background).into(binding.ivThumnail)


    }
}
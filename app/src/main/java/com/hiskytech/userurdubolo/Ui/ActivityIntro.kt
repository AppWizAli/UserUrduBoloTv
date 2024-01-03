package com.hiskytech.userurdubolo.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hiskytech.userurdubolo.R

class ActivityIntro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_intro)
    }
}
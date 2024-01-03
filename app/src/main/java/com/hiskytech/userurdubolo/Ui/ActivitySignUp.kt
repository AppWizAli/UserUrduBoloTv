package com.hiskytech.userurdubolo.Ui
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.hiskytech.userurdubolo.R

class ActivitySignUp : AppCompatActivity() {
    private lateinit var createAccountText: TextView
    private lateinit var iconTransparent: ImageView
    private val colors = listOf("#FEC10F", "#FFFFFFFF", "#F1054E")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_sign_up)

        createAccountText = findViewById(R.id.createAccountText)
        iconTransparent = findViewById(R.id.icon_transparent)

        val customFont = ResourcesCompat.getFont(this, R.font.myfont)
        createAccountText.typeface = customFont

        // Start the text animation
        animateTextWithDifferentColors(
            "Welcome to UrduBolo Tv! Create an account to explore exclusive content......",
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
}


package com.danialtavakoli.projects.quizapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.danialtavakoli.projects.quizapp.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")
        val correctIndex = intent.getIntExtra("correctAnswer", 0)
        val wrongIndex = intent.getIntExtra("wrongAnswer", 0)
        val maxScore = intent.getIntExtra("maxScore", 0)
        val percentScore = intent.getDoubleExtra("percentScore", 0.0)
        with(binding) {
            if (maxScore == correctIndex) {
                textViewUsername.visibility = View.GONE
                textViewCorrect.visibility = View.GONE
                textViewWrong.visibility = View.GONE
                textViewPercent.visibility = View.GONE
                return
            }
            textViewUsername.text = username
            "درست : $correctIndex".also { textViewCorrect.text = it }
            "غلط : $wrongIndex".also { textViewWrong.text = it }
            "درصد پاسخ اشتباه : $percentScore".also { textViewPercent.text = it }
        }

    }
}
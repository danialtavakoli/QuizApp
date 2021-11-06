package com.danialtavakoli.projects.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.danialtavakoli.projects.quizapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSubmitUsername.setOnClickListener {
            if (binding.editTextUsername.text.toString().isEmpty())
                Toast.makeText(this, "نام کاربری را وارد کنید", Toast.LENGTH_SHORT).show()
            else {
                val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("username", binding.editTextUsername.text.toString())
                startActivity(intent)
            }
        }
    }
}
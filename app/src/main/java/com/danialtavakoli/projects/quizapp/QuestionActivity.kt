package com.danialtavakoli.projects.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.danialtavakoli.projects.quizapp.databinding.ActivityQuestionBinding

class QuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionBinding
    private lateinit var questionList: ArrayList<Question>
    private var currentQuestion = 0
    private var selectedOption = 0
    private var correctAnswer = 0
    private var wrongAnswer = 0
    private var percentScore = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        questionList = Constants.getQuestions()
        loadQuestionData()
        val username = intent.getStringExtra("username")
        binding.textViewName.text = username
        binding.buttonShowResult.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            calculatePercentScore()
            intent.putExtra("maxScore", binding.progressBar.max)
            intent.putExtra("username", username)
            intent.putExtra("wrongAnswer", wrongAnswer)
            intent.putExtra("percentScore", percentScore)
            calculateFinalResult()
            intent.putExtra("correctAnswer", correctAnswer)
            startActivity(intent)
        }
    }

    private fun loadQuestionData() {
        val question = questionList[currentQuestion]
        with(binding) {
            textViewQuestionTitle.text = question.question
            imageViewFlag.setImageResource(question.image)
            textViewOptionOne.text = question.optionOne
            textViewOptionTwo.text = question.optionTwo
            textViewOptionThree.text = question.optionThree
            textViewOptionFour.text = question.optionFour
            progressBar.progress = currentQuestion + 1
            "${currentQuestion + 1} / ${binding.progressBar.max}".also { textViewProcess.text = it }
            if (correctAnswer >= wrongAnswer)
                textViewName.setTextColor(Color.parseColor("#43a047"))
            else
                textViewName.setTextColor(Color.parseColor("#d50000"))
        }
        binding.textViewOptionOne.isEnabled = true
        binding.textViewOptionTwo.isEnabled = true
        binding.textViewOptionThree.isEnabled = true
        binding.textViewOptionFour.isEnabled = true
    }

    private fun setDefaultForOption() {
        val textViewOptions = ArrayList<TextView>()
        with(binding) {
            textViewOptions.add(textViewOptionOne)
            textViewOptions.add(textViewOptionTwo)
            textViewOptions.add(textViewOptionThree)
            textViewOptions.add(textViewOptionFour)
        }
        for (textViewOption in textViewOptions) {
            textViewOption.typeface = Typeface.DEFAULT
            textViewOption.background =
                ContextCompat.getDrawable(this, R.drawable.options_background)
            textViewOption.setTextColor(Color.parseColor("#7a8089"))
        }
    }

    fun onOptionClicked(view: View) {
        setDefaultForOption()
        val selectedTextView = view as TextView
        when (selectedTextView.tag.toString()) {
            "optionOne" -> selectedOption = 1
            "optionTwo" -> selectedOption = 2
            "optionThree" -> selectedOption = 3
            "optionFour" -> selectedOption = 4
        }
        selectedTextView.typeface = Typeface.DEFAULT_BOLD
        selectedTextView.background =
            ContextCompat.getDrawable(this, R.drawable.options_background_selected)
        selectedTextView.setTextColor(Color.parseColor("#000000"))
    }

    fun submitAnswer(view: View) {
        binding.textViewOptionOne.isEnabled = false
        binding.textViewOptionTwo.isEnabled = false
        binding.textViewOptionThree.isEnabled = false
        binding.textViewOptionFour.isEnabled = false
        if (selectedOption == 0)
            binding.button.text = "جوابمو انتخاب کردم!"
        if (selectedOption == 0 && currentQuestion < questionList.size - 1) {
            currentQuestion++
            loadQuestionData()
            setDefaultForOption()
        } else {
            if (currentQuestion == questionList.size - 1 && selectedOption == 0) {
                binding.button.visibility = View.GONE
                binding.buttonShowResult.visibility = View.VISIBLE
                return
            }
            checkAnswers()
            binding.button.text = "برو به سوال بعدی"
            selectedOption = 0
            if (currentQuestion == questionList.size - 1) {
                binding.button.visibility = View.GONE
                binding.buttonShowResult.visibility = View.VISIBLE
            }
        }
    }

    private fun setBackgroundForTextViews(optionIndex: Int, drawableIndex: Int) {
        when (optionIndex) {
            1 -> binding.textViewOptionOne.background =
                ContextCompat.getDrawable(this, drawableIndex)
            2 -> binding.textViewOptionTwo.background =
                ContextCompat.getDrawable(this, drawableIndex)
            3 -> binding.textViewOptionThree.background =
                ContextCompat.getDrawable(this, drawableIndex)
            4 -> binding.textViewOptionFour.background =
                ContextCompat.getDrawable(this, drawableIndex)
        }
    }

    private fun checkAnswers() {
        if (questionList[currentQuestion].correctAnswer == selectedOption) {
            setBackgroundForTextViews(selectedOption, R.drawable.options_background_correct)
            correctAnswer++
        } else {
            setBackgroundForTextViews(selectedOption, R.drawable.options_background_wrong)
            setBackgroundForTextViews(
                questionList[currentQuestion].correctAnswer,
                R.drawable.options_background_correct
            )
            wrongAnswer++
        }
    }

    private fun calculateFinalResult() {
        val ratio = wrongAnswer / 3
        if (ratio % 1 == 0) {
            if (correctAnswer > 0)
                correctAnswer -= ratio
            wrongAnswer -= ratio * 3
        }
    }

    private fun calculatePercentScore() {
        this.percentScore = wrongAnswer.toDouble() / binding.progressBar.max.toDouble() * 100
    }
}
package com.bignerdranch.android.geomain

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    // MainActivity - имя класса
    // AppCompatActivity() - тип класса
    // Activity - по сути просто контроллер
    // xml - представление
    // data - модель

    private lateinit var trueButton: Button //lateinit - lazy initialization, так как созданы будут после вызова onCreate
    private lateinit var falseButton: Button //var - variable, val - value = final
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var answerCountTextView: TextView
    private var answerCount: Int = 0
    private var correctAnswerCount: Int = 0

    private val questionBank = listOf(
        Question(R.string.question_australia, true, null),
        Question(R.string.question_oceans, true, null),
        Question(R.string.question_mideast, false, null),
        Question(R.string.question_africa, false, null),
        Question(R.string.question_americas, true, null),
        Question(R.string.question_asia, true, null)
    )
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        // fun - функция
        // savedInstanceState - аргумет
        // Bundle - тип аргумента
        // ? - значит, что может прийти null
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main) //предоставление пользовательского интерфейса для Activity,
        // R - хранилище id для виджетов (автогенерируемое)

        trueButton = findViewById(R.id.true_button) //там все наследуется от View = виджет
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button) //находим представления и связываем с объектами контроллера
        prevButton = findViewById(R.id.prev_button)
        //View <- Controller <- Model

        questionTextView = findViewById(R.id.question_text_view)
        answerCountTextView = findViewById(R.id.answer_count_view)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true, view)
            answerCount++
            checkAnswersDone()
            /*
            var toast: Toast = Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 0, 0) //deprecated since API 30
            toast.show()
            */
            //Toast - виджет андроида, this - для этого активити -
            // - Параметр Context обычно является экземпляром Activity (наследуется от Context)
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false, view)
            answerCount++
            checkAnswersDone()
        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size //0 раз с остатком в currentIndex
            // пока не будет 1 с остатком 0
            updateQuestion()
        }

        prevButton.setOnClickListener {
            if (currentIndex == 0) {
                currentIndex = questionBank.size - 1
            } else {
                currentIndex -= 1
            }
            updateQuestion()
        }

        questionTextView.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
        updateQuestion() //установка текста при инициализации
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId //вытаскивает value textResId
        questionTextView.setText(questionTextResId) //установка текста в виджет в зависимости от значения итератора списка вопросов
        setDefaultButtons()
        if (questionBank[currentIndex].userAnswer != null) {
            setButtons(
                questionBank[currentIndex].userAnswer == true,
                questionBank[currentIndex].answer
            )
        }
    }

    private fun checkAnswer(userAnswer: Boolean, view: View) {
        //userAnswer = false from falseButton, true from trueButton
        val correctAnswer = questionBank[currentIndex].answer //чекаем булеан на текущий вопрос

        if (userAnswer == correctAnswer) {
            Snackbar.make(view, R.string.correct_toast, Snackbar.LENGTH_SHORT).show()
            correctAnswerCount++
        } else {
            Snackbar.make(view, R.string.incorrect_toast, Snackbar.LENGTH_SHORT).show()
        }

        setButtons(userAnswer, correctAnswer)
    }

    private fun setButtons(userAnswer: Boolean, correctAnswer: Boolean) {
        if (userAnswer && correctAnswer) {
            trueButton.setBackgroundColor(Color.GREEN)
        } else if (!userAnswer && !correctAnswer) {
            falseButton.setBackgroundColor(Color.GREEN)
        } else if (userAnswer && !correctAnswer) {
            trueButton.setBackgroundColor(Color.RED)
        } else {
            falseButton.setBackgroundColor(Color.RED)
        }
        falseButton.isEnabled = false
        trueButton.isEnabled = false
        questionBank[currentIndex].userAnswer = userAnswer;
    }

    private fun setDefaultButtons() {
        trueButton.setBackgroundColor(Color.BLUE)
        trueButton.isEnabled = true
        trueButton.setTextColor(Color.WHITE)
        falseButton.setBackgroundColor(Color.BLUE)
        falseButton.isEnabled = true
        falseButton.setTextColor(Color.WHITE)
    }

    private fun checkAnswersDone() {
        if (answerCount == questionBank.size) {
            answerCountTextView.text = "You done " + correctAnswerCount + "/" + questionBank.size + "!"
        }
    }

}
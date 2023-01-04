package com.bignerdranch.android.geomain

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    // MainActivity - имя класса
    // AppCompatActivity() - тип класса
    // Activity - по сути просто контроллер
    // xml - представление
    // data - модель

    private lateinit var trueButton: Button //lateinit - lazy initialization, так как созданы будут после вызова onCreate
    private lateinit var falseButton: Button //var - variable, val - value = final
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        // fun - функция
        // savedInstanceState - аргумет
        // Bundle - тип аргумента
        // ? - значит, что может прийти null
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //предоставление пользовательского интерфейса для Activity,
        // R - хранилище id для виджетов (автогенерируемое)

        trueButton = findViewById(R.id.true_button) //там все наследуется от View = виджет
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button) //находим представления и связываем с объектами контроллера
        //View <- Controller <- Model

        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {view: View ->

           var snackbar: Snackbar = Snackbar.make(view, R.string.correct_toast, Toast.LENGTH_SHORT)
           snackbar.show()

            /*
            var toast: Toast = Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 0, 0) //deprecated since API 30
            toast.show()
            */
            //Toast - виджет андроида, this - для этого активити -
            // - Параметр Context обычно является экземпляром Activity (наследуется от Context)

        }
        falseButton.setOnClickListener {view: View ->
            var snackbar: Snackbar = Snackbar.make(view, R.string.incorrect_toast, Toast.LENGTH_SHORT)
            snackbar.show()
        }

        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId) //установка текста в виджет в зависимости от значения итератора
        //списка вопросов

    }
}
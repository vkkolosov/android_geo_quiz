package com.bignerdranch.android.geomain

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val ANSWER_DATA = "data"
private const val ANSWER_COUNT = "answer count"
private const val CORRECT_ANSWER_COUNT = "correct answer count"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    // MainActivity - имя класса
    // AppCompatActivity() - тип класса
    // Activity - по сути просто контроллер
    // xml - представление
    // data - модель

    //как все работает:
    //1. Создается список вопросов, туда кидаются айдишники вопросов из strings.xml, и ответы
    //2. Создается индекс массива
    //3. При onCreate():
    //      - устанавливается View activity_main.xml (там заранее прописаны дефолтные значения текста и кнопок)
    //      - привязываются кнопки, текст, теперь на View будут отображаться изменения на стороне back-end
    //      - ставятся листенеры на кнопки, которые сверяют индекс и отвечал ли юзер уже на этот вопрос
    //        в updateQuestion() + общий счетчик

    private lateinit var trueButton: Button //lateinit - lazy initialization, так как созданы будут после вызова onCreate
    private lateinit var falseButton: Button //var - variable, val - value = final
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var answerCountTextView: TextView
    private lateinit var cheatButton: Button

    //ViewModelProvider работает как реестр ViewModel
    //ViewModel - класс, позволяющий Activity и фрагментам сохранять необходимые им объекты живыми при повороте экрана.
    //при поворотах экрана, Activity будет пересоздаваться, а объект MyViewModel будет спокойно себе жить в провайдере
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        // fun - функция
        // savedInstanceState - аргумет
        // Bundle - тип аргумента
        // ? - значит, что может прийти null
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main) //предоставление пользовательского интерфейса для Activity,
        // R - хранилище id для виджетов (автогенерируемое)

        //Блок синхронизации с savedInstanceState
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0)
            ?: 0 //?: – оператор "элвис", 0, если пришел null
        //возможно, что это вообще не нужно, надо потестить
        val questionBank = savedInstanceState?.getParcelableArrayList(ANSWER_DATA) ?: arrayListOf(
            Question(R.string.question_australia, true, null),
            Question(R.string.question_oceans, true, null),
            Question(R.string.question_mideast, false, null),
            Question(R.string.question_africa, false, null),
            Question(R.string.question_americas, true, null),
            Question(R.string.question_asia, true, null)
        )
        val answerCount = savedInstanceState?.getInt(ANSWER_COUNT, 0) ?: 0
        val correctAnswerCount = savedInstanceState?.getInt(CORRECT_ANSWER_COUNT, 0) ?: 0
        quizViewModel.currentIndex = currentIndex
        quizViewModel.questionBank = questionBank
        quizViewModel.answerCount = answerCount
        quizViewModel.correctAnswerCount = correctAnswerCount

        trueButton = findViewById(R.id.true_button) //там все наследуется от View = виджет
        falseButton = findViewById(R.id.false_button)
        nextButton =
            findViewById(R.id.next_button) //находим представления и связываем с объектами контроллера
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        //View <- Controller <- Model

        questionTextView = findViewById(R.id.question_text_view)
        answerCountTextView = findViewById(R.id.answer_count_view)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true, view)
            quizViewModel.answerCount++
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
            quizViewModel.answerCount++
            checkAnswersDone()
        }

        nextButton.setOnClickListener {
            quizViewModel.currentIndex =
                (quizViewModel.currentIndex + 1) % quizViewModel.questionBank.size //0 раз с остатком в currentIndex
            // пока не будет 1 с остатком 0
            updateQuestion()
        }

        prevButton.setOnClickListener {
            if (quizViewModel.currentIndex == 0) {
                quizViewModel.currentIndex = quizViewModel.questionBank.size - 1
            } else {
                quizViewModel.currentIndex -= 1
            }
            updateQuestion()
        }

        questionTextView.setOnClickListener {
            quizViewModel.currentIndex =
                (quizViewModel.currentIndex + 1) % quizViewModel.questionBank.size
            updateQuestion()
        }
        updateQuestion() //установка текста при инициализации
        checkAnswersDone()

        cheatButton.setOnClickListener { view ->
            /*
            //это явный Intent через ОС
            val intent = Intent(this, CheatActivity::class.java)
            */
            val answerIsTrue = quizViewModel.questionBank[quizViewModel.currentIndex].answer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val options =
                    ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                //startActivity(intent) //Передается задача ОС (Activity manager), она вызывает экземпляр CheatActivity
                startActivityForResult(
                    intent,
                    REQUEST_CODE_CHEAT,
                    options.toBundle()
                ) //MainActivity становится «родителем» для NameActivity
            } else {
                startActivityForResult(
                    intent,
                    REQUEST_CODE_CHEAT
                )
            }

        }
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

    //ОС может просто закрыть низкоприоритетные приложения при недостатке памяти
    //потому нужно сохранять состояния
    //Don’tkeepactivities позволяет это затестить
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        savedInstanceState.putParcelableArrayList(ANSWER_DATA, quizViewModel.questionBank)
        savedInstanceState.putInt(ANSWER_COUNT, quizViewModel.answerCount)
        savedInstanceState.putInt(CORRECT_ANSWER_COUNT, quizViewModel.correctAnswerCount)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    private fun updateQuestion() {
        val questionTextResId =
            quizViewModel.questionBank[quizViewModel.currentIndex].textResId //вытаскивает value textResId
        questionTextView.setText(questionTextResId) //установка текста в виджет в зависимости от значения итератора списка вопросов
        setDefaultButtons()
        if (quizViewModel.questionBank[quizViewModel.currentIndex].userAnswer != null) {
            setButtons(
                quizViewModel.questionBank[quizViewModel.currentIndex].userAnswer == true,
                quizViewModel.questionBank[quizViewModel.currentIndex].answer
            )
        }
        quizViewModel.isCheater = false
    }

    private fun checkAnswer(userAnswer: Boolean, view: View) {
        //userAnswer = false from falseButton, true from trueButton
        val correctAnswer =
            quizViewModel.questionBank[quizViewModel.currentIndex].answer //чекаем булеан на текущий вопрос

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Snackbar.make(view, messageResId, Snackbar.LENGTH_SHORT).show()

        if (userAnswer == correctAnswer) quizViewModel.correctAnswerCount++

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
        quizViewModel.questionBank[quizViewModel.currentIndex].userAnswer = userAnswer
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
        if (quizViewModel.answerCount == quizViewModel.questionBank.size) {
            answerCountTextView.text =
                "You done " + quizViewModel.correctAnswerCount + "/" + quizViewModel.questionBank.size + "!"
        }
    }

}
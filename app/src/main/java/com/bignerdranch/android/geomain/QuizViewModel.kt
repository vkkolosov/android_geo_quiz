package com.bignerdranch.android.geomain

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel(){

    init {
        Log.d(TAG, "ViewModel instance created")
    }

    var answerCount: Int = 0
    var correctAnswerCount: Int = 0

    var questionBank = arrayListOf(
        Question(R.string.question_australia, true, null),
        Question(R.string.question_oceans, true, null),
        Question(R.string.question_mideast, false, null),
        Question(R.string.question_africa, false, null),
        Question(R.string.question_americas, true, null),
        Question(R.string.question_asia, true, null)
    )
    var currentIndex = 0

    //Модель жива, пока Activity не закроется окончательно. Т.е. приложение не закроют.
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }

}
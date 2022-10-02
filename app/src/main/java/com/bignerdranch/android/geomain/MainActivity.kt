package com.bignerdranch.android.geomain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    // MainActivity - имя класса
    // AppCompatActivity() - тип класса
    override fun onCreate(savedInstanceState: Bundle?) {
        // fun - функция
        // savedInstanceState - аргумет
        // Bundle - тип аргумента
        // ? - значит, что может прийти null
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
package com.bignerdranch.android.geomain

import androidx.annotation.StringRes

data class Question(@StringRes val textResId:
                    Int, val answer: Boolean)

/*
@StringRes - проверяет, что будет именно строковой ресурс, а не какой-нибудь рандомный

Data - класс данных

Компилятор автоматически формирует следующие члены данного класса из свойств, объявленных в основном конструкторе:

пару функций equals()/hashCode(),
функцию toString() в форме "User(name=John, age=42)",
компонентные функции componentN(), которые соответствуют свойствам, в соответствии с порядком их объявления,
функцию copy()
 */
package com.bignerdranch.android.geomain

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean, var userAnswer: Boolean?) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(textResId)
        parcel.writeByte(if (answer) 1 else 0)
        parcel.writeValue(userAnswer)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}

/*
@StringRes - проверяет, что будет именно строковой ресурс, а не какой-нибудь рандомный

Data - класс данных

Компилятор автоматически формирует следующие члены данного класса из свойств, объявленных в основном конструкторе:

пару функций equals()/hashCode(),
функцию toString() в форме "User(name=John, age=42)",
компонентные функции componentN(), которые соответствуют свойствам, в соответствии с порядком их объявления,
функцию copy()
 */
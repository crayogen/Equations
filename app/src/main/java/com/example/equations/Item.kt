package com.example.equations

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class Item : Parcelable {
    abstract val isRequired: Boolean

    fun isNumber() = this is Number
    fun isOperator() = this is Operator

    @Parcelize
    class Number(
        val number: Int,
        override val isRequired: Boolean = false,
        val equation: Equation? = null
    ) : Item() {
        override fun toString() = number.toString()
    }

    sealed class Operator : Item() {
        fun operate(number1: Number, number2: Number): Number? {
            return Number(
                compute(number1.number, number2.number) ?: return null,
                isRequired || number1.isRequired || number2.isRequired,
                Equation(number1, number2, this)
            )
        }

        protected abstract fun compute(number1: Int, number2: Int): Int?

        @Parcelize
        class Plus(override val isRequired: Boolean = false) : Operator() {
            override fun toString() = "+"
            override fun compute(number1: Int, number2: Int) = number1 + number2
        }

        @Parcelize
        class Minus(override val isRequired: Boolean = false) : Operator() {
            override fun toString() = "-"
            override fun compute(number1: Int, number2: Int) = number1 - number2
        }

        @Parcelize
        class Multiply(override val isRequired: Boolean = false) : Operator() {
            override fun toString() = "x"
            override fun compute(number1: Int, number2: Int) = number1 * number2
        }

        @Parcelize
        class Power(override val isRequired: Boolean = false) : Operator() {
            override fun toString() = "^"
            override fun compute(number1: Int, number2: Int) =
                Math.pow(number1.toDouble(), number2.toDouble()).toInt()
        }

        @Parcelize
        class Mod(override val isRequired: Boolean = false) : Operator() {
            override fun toString() = "//"
            override fun compute(number1: Int, number2: Int) =
                if (number2 == 0) null else number1 % number2
        }
    }
}

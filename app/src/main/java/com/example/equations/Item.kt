package com.example.equations

sealed class Item {
    fun isNumber() = this is Number
    fun isOperator() = this is Operator

    class Number(val number: Int, val equation: Equation? = null) : Item() {
        override fun toString() = number.toString()
    }

    sealed class Operator : Item() {
        fun operate(number1: Number, number2: Number) = Number(
            compute(number1.number, number2.number),
            Equation(number1, number2, this)
        )

        protected abstract fun compute(number1: Int, number2: Int): Int

        object Plus : Operator() {
            override fun toString() = "+"
            override fun compute(number1: Int, number2: Int) = number1 + number2
        }

        object Minus : Operator() {
            override fun toString() = "-"
            override fun compute(number1: Int, number2: Int) = number1 - number2
        }

        object Multiply : Operator() {
            override fun toString() = "x"
            override fun compute(number1: Int, number2: Int) = number1 * number2
        }

        object Power : Operator() {
            override fun toString() = "^"
            override fun compute(number1: Int, number2: Int) =
                Math.pow(number1.toDouble(), number2.toDouble()).toInt()
        }
    }
}

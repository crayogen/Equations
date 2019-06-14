package com.example.equations

sealed class Item {
    class Number(val number: Int, val equation: Equation? = null) : Item() {
        override fun toString() = number.toString()
    }

    sealed class Operator : Item() {
        fun operate(number1: Number, number2: Number) = Item.Number(
            compute(number1, number2),
            Equation(number1, number2, this)
        )

        protected abstract fun compute(number1: Number, number2: Number): Int

        object Plus : Operator() {
            override fun compute(number1: Number, number2: Number) =
                number1.number + number2.number

            override fun toString() = "+"
        }

        object Minus : Operator() {
            override fun compute(number1: Number, number2: Number) =
                number1.number - number2.number

            override fun toString() = "-"
        }

        object Multiply : Operator() {
            override fun compute(number1: Number, number2: Number) =
                number1.number * number2.number

            override fun toString() = "x"
        }

        object Power : Operator() {
            override fun compute(number1: Number, number2: Number) =
                Math.pow(number1.number.toDouble(), number2.number.toDouble()).toInt()

            override fun toString() = "^"
        }
    }
}

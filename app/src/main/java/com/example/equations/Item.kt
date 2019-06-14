package com.example.equations

sealed class Item {
    class Number(val number: Int) : Item() {
        override fun toString() = number.toString()
    }

    sealed class Operator : Item() {
        abstract fun compute(number1: Number, number2: Number): Number

        object Plus : Operator() {
            override fun compute(number1: Number, number2: Number) =
                Number(number1.number + number2.number)

            override fun toString() = "+"
        }

        object Minus : Operator() {
            override fun compute(number1: Number, number2: Number) =
                Number(number1.number - number2.number)

            override fun toString() = "-"
        }

        object Multiply : Operator() {
            override fun compute(number1: Number, number2: Number) =
                Number(number1.number * number2.number)

            override fun toString() = "x"
        }

        object Power : Operator() {
            override fun compute(number1: Number, number2: Number) =
                Number(Math.pow(number1.number.toDouble(), number2.number.toDouble()).toInt())

            override fun toString() = "^"
        }
    }
}

package com.example.equations

sealed class Item {
    abstract val isNecessary: Boolean

    fun isNumber() = this is Number
    fun isOperator() = this is Operator

    class Number(
        val number: Int,
        override val isNecessary: Boolean = false,
        val equation: Equation? = null
    ) : Item() {
        override fun toString() = number.toString()
    }

    sealed class Operator : Item() {
        fun operate(number1: Number, number2: Number) = Number(
            compute(number1.number, number2.number),
            isNecessary || number1.isNecessary || number2.isNecessary,
            Equation(number1, number2, this)
        )

        protected abstract fun compute(number1: Int, number2: Int): Int

        class Plus(override val isNecessary: Boolean = false) : Operator() {
            override fun toString() = "+"
            override fun compute(number1: Int, number2: Int) = number1 + number2
        }

        class Minus(override val isNecessary: Boolean = false) : Operator() {
            override fun toString() = "-"
            override fun compute(number1: Int, number2: Int) = number1 - number2
        }

        class Multiply(override val isNecessary: Boolean = false) : Operator() {
            override fun toString() = "x"
            override fun compute(number1: Int, number2: Int) = number1 * number2
        }

        class Power(override val isNecessary: Boolean = false) : Operator() {
            override fun toString() = "^"
            override fun compute(number1: Int, number2: Int) =
                Math.pow(number1.toDouble(), number2.toDouble()).toInt()
        }
    }
}

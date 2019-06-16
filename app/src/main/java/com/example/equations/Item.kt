package com.example.equations

sealed class Item {
    abstract val isRequired: Boolean

    fun isNumber() = this is Number
    fun isOperator() = this is Operator

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

        class Plus(override val isRequired: Boolean = false) : Operator() {
            override fun toString() = "+"
            override fun compute(number1: Int, number2: Int) = number1 + number2
        }

        class Minus(override val isRequired: Boolean = false) : Operator() {
            override fun toString() = "-"
            override fun compute(number1: Int, number2: Int) = number1 - number2
        }

        class Multiply(override val isRequired: Boolean = false) : Operator() {
            override fun toString() = "x"
            override fun compute(number1: Int, number2: Int) = number1 * number2
        }

        class Power(override val isRequired: Boolean = false) : Operator() {
            override fun toString() = "^"
            override fun compute(number1: Int, number2: Int) =
                Math.pow(number1.toDouble(), number2.toDouble()).toInt()
        }

        class Mod(override val isRequired: Boolean = false) : Operator() {
            override fun toString() = "//"
            override fun compute(number1: Int, number2: Int) =
                if (number2 == 0) null else number1 % number2
        }
    }
}

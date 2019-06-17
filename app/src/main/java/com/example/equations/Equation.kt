package com.example.equations

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Equation(
    val firstNumber: Item.Number,
    val secondNumber: Item.Number,
    val operator: Item.Operator
) : Parcelable

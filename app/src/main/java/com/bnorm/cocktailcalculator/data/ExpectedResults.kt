package com.bnorm.cocktailcalculator.data

data class ExpectedResults(
        val dilution: ClosedFloatingPointRange<Double>,
        val volume: ClosedFloatingPointRange<Double>,
        val ethanol: ClosedFloatingPointRange<Double>,
        val sugar: ClosedFloatingPointRange<Double>,
        val acid: ClosedFloatingPointRange<Double>
)

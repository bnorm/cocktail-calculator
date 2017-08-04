package com.bnorm.cocktailcalculator.data

enum class Techniques(val str: String) {
    Shaken("Shaken") {
        override val expected = ExpectedResults(0.51..0.6, 5.2..5.9, 0.15..0.2, 5.0..8.9, 0.0076..0.0094)
        override val preparing = "Shake with 120 grams of 1.25 inch ice cubes (~4 cubes) for 10 seconds"
        override fun dilution(ethanol: Double) = -1.567 * ethanol * ethanol + 1.742 * ethanol + 0.203
    },

    ShakenWithEggWhite("Shaken with egg white") {
        override val expected = ExpectedResults(0.46..0.49, 6.6..7.0, 0.12..0.15, 6.7..9.0, 0.0049..0.0068)
        override val preparing = "Shake with 120 grams of 1.25 inch ice cubes (~4 cubes) for 10 seconds, strain out ice, then add egg white and dry shake for 10 seconds"
        override fun dilution(ethanol: Double) = -1.567 * ethanol * ethanol + 1.742 * ethanol + 0.203
    },

    Stirred("Stirred") {
        override val expected = ExpectedResults(0.41..0.49, 4.3..4.75, 0.21..0.29, 3.7..5.6, 0.0..0.002)
        override val preparing = "Stir quickly with 120 grams of 1.25 inch ice cubes (~4 cubes) for 15 seconds"
        override fun dilution(ethanol: Double) = -1.21 * ethanol * ethanol + 1.246 * ethanol + 0.145
    },

    Built("Built") {
        override val expected = ExpectedResults(0.24..0.24, 2.9..3.1, 0.27..0.32, 7.0..8.0, 0.0..0.0)
        override val preparing = "Stir directly in your cocktail glass with 120 grams of 1.25 inch ice cubes (~4 cubes) for 15 seconds"
        override fun dilution(ethanol: Double): Double = 0.24
    },

    Carbonated("Carbonated") {
        override val expected = ExpectedResults(0.0..0.0, 5.0..6.0, 0.14..0.16, 5.0..7.5, 0.0038..0.0051)
        override val preparing = "Pour ingredients into glass with ice, quick stir to mix ingredients"
        override fun dilution(ethanol: Double) = 0.0
    },

    // End of enumeration
    ;

    companion object {
        val map = values().map { it.str to it }.toMap()
    }

    abstract val expected: ExpectedResults
    abstract val preparing: String
    abstract fun dilution(ethanol: Double): Double
}

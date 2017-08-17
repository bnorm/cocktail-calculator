package com.bnorm.cocktailcalculator.data

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.bnorm.cocktailcalculator.R

enum class RecipeComponent(
        private val smaller: String,
        private val larger: String
) {

    Dilution("Underdiluted", "Overdiluted") {
        override val RecipeResults.value: Double get() = dilution
        override val ExpectedResults.range: ClosedFloatingPointRange<Double> get() = dilution

        override fun format(value: Double): String = "${round(value * 100)}%"
    },

    Volume("Not enough volume", "Too much volume") {
        override val RecipeResults.value: Double get() = volume
        override val ExpectedResults.range: ClosedFloatingPointRange<Double> get() = volume

        override fun format(value: Double): String = "${round(value)}"
    },

    Ethanol("Not enough ethanol", "Too much ethanol") {
        override val RecipeResults.value: Double get() = ethanol
        override val ExpectedResults.range: ClosedFloatingPointRange<Double> get() = ethanol

        override fun format(value: Double): String = "${round(value * 100)}%"
    },

    Sugar("Not sweet enough", "Too sweet") {
        override val RecipeResults.value: Double get() = sugar
        override val ExpectedResults.range: ClosedFloatingPointRange<Double> get() = sugar

        override fun format(value: Double): String = "${round(value)}"
    },

    Acid("Not acidic enough", "Too acidic") {
        override val RecipeResults.value: Double get() = acid
        override val ExpectedResults.range: ClosedFloatingPointRange<Double> get() = acid

        override fun format(value: Double): String = "${round(value * 100)}%"
    },

    // End of enumeration
    ;

    protected abstract val RecipeResults.value: Double
    protected abstract val ExpectedResults.range: ClosedFloatingPointRange<Double>

    protected abstract fun format(value: Double): String

    fun set(expected: ExpectedResults, result: RecipeResults, numberView: TextView, messageView: TextView) {
        numberView.setBackgroundColor(color(numberView, expected, result))
        numberView.text = if (result.value == Double.NaN) "-" else format(result.value)

        messageView.setBackgroundColor(color(messageView, expected, result))
        messageView.text = when {
            result.value in expected.range -> "Good (${format(expected.range.start)}-${format(expected.range.endInclusive)})"
            result.value < expected.range.start -> "$smaller (<${format(expected.range.start)})"
            result.value > expected.range.endInclusive -> "$larger (>${format(expected.range.endInclusive)})"
            else -> "Undefined (${format(expected.range.start)}-${format(expected.range.endInclusive)})"
        }
    }

    private fun color(view: View, expected: ExpectedResults, result: RecipeResults): Int {
        return when (result.value) {
            in expected.range -> ContextCompat.getColor(view.context, R.color.resultGood)
            else -> ContextCompat.getColor(view.context, R.color.resultBad)
        }
    }

    protected fun round(num: Double) = Math.round(num * 100) / 100.0
}
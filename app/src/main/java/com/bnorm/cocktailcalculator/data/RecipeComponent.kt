package com.bnorm.cocktailcalculator.data

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.bnorm.cocktailcalculator.R

enum class RecipeComponent {
    Dilution {
        override fun color(view: View, expected: ExpectedResults, result: RecipeResults): Int
                = view.color(expected.dilution, result.dilution)

        override fun format(value: Double): String
                = "${round(value * 100)}%"

        override fun number(result: RecipeResults): String
                = if (result.dilution == Double.NaN) "-" else format(result.dilution)

        override fun message(expected: ExpectedResults, result: RecipeResults): String
                = message(expected.dilution, result.dilution, "Underdiluted", "Overdiluted")
    },

    Volume {
        override fun color(view: View, expected: ExpectedResults, result: RecipeResults): Int
                = view.color(expected.volume, result.volume)

        override fun format(value: Double): String
                = "${round(value)}"

        override fun number(result: RecipeResults): String
                = if (result.volume == Double.NaN) "-" else format(result.volume)

        override fun message(expected: ExpectedResults, result: RecipeResults): String
                = message(expected.volume, result.volume, "Not enough volume", "Too much volume")
    },

    Ethanol {
        override fun color(view: View, expected: ExpectedResults, result: RecipeResults): Int
                = view.color(expected.ethanol, result.ethanol)

        override fun format(value: Double): String
                = "${round(value * 100)}%"

        override fun number(result: RecipeResults): String
                = if (result.ethanol == Double.NaN) "-" else format(result.ethanol)

        override fun message(expected: ExpectedResults, result: RecipeResults): String
                = message(expected.ethanol, result.ethanol, "Not enough ethanol", "Too much ethanol")
    },

    Sugar {
        override fun color(view: View, expected: ExpectedResults, result: RecipeResults): Int
                = view.color(expected.sugar, result.sugar)

        override fun format(value: Double): String
                = "${round(value)}"

        override fun number(result: RecipeResults): String
                = if (result.sugar == Double.NaN) "-" else format(result.sugar)

        override fun message(expected: ExpectedResults, result: RecipeResults): String
                = message(expected.sugar, result.sugar, "Not sweet enough", "Too sweet")
    },

    Acid {
        override fun color(view: View, expected: ExpectedResults, result: RecipeResults): Int
                = view.color(expected.acid, result.acid)

        override fun format(value: Double): String
                = "${round(value * 100)}%"

        override fun number(result: RecipeResults): String
                = if (result.acid == Double.NaN) "-" else format(result.acid)

        override fun message(expected: ExpectedResults, result: RecipeResults): String
                = message(expected.acid, result.acid, "Not acidic enough", "Too acidic")
    },

    // End of enumeration
    ;

    fun set(expected: ExpectedResults, result: RecipeResults, numberView: TextView, messageView: TextView) {
        numberView.text = number(result)
        numberView.setBackgroundColor(color(numberView, expected, result))

        messageView.text = message(expected, result)
        messageView.setBackgroundColor(color(messageView, expected, result))
    }

    protected abstract fun format(value: Double): String

    protected abstract fun number(result: RecipeResults): String

    protected abstract fun message(expected: ExpectedResults, result: RecipeResults): String

    protected abstract fun color(view: View, expected: ExpectedResults, result: RecipeResults): Int

    protected fun message(range: ClosedFloatingPointRange<Double>,
                          value: Double,
                          smaller: String,
                          larger: String): String {
        return when {
            value in range -> "Good (${format(range.start)}-${format(range.endInclusive)})"
            value < range.start -> "$smaller (<${format(range.start)})"
            value > range.endInclusive -> "$larger (>${format(range.endInclusive)})"
            else -> "Undefined (${format(range.start)}-${format(range.endInclusive)})"
        }
    }

    protected fun round(num: Double) = Math.round(num * 100) / 100.0

    protected fun <T : Comparable<T>> View.color(range: ClosedRange<T>, value: T) = when (value) {
        in range -> ContextCompat.getColor(this.context, R.color.resultGood)
        else -> ContextCompat.getColor(this.context, R.color.resultBad)
    }
}
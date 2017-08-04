package com.bnorm.cocktailcalculator.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.bnorm.cocktailcalculator.data.db.Ingredient
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

data class IngredientAmount(var id: Long = -1,
                            var ingredient: Ingredient? = null,
                            var amount: Double = 0.0)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val data = ArrayList<IngredientAmount>().apply {
        add(IngredientAmount(0, null, 0.0))
    }

    private val subject: Subject<List<IngredientAmount>> = BehaviorSubject.createDefault(data.toList())

    val ingredients: Observable<List<IngredientAmount>>
        get() = subject

    fun addIngredient() {
        data.add(IngredientAmount(data.size.toLong(), null, 0.0))
        subject.onNext(data.toList())
    }
}

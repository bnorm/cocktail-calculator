package com.bnorm.cocktailcalculator.ui

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.ArrayAdapter
import com.bnorm.cocktailcalculator.R
import com.bnorm.cocktailcalculator.data.RecipeComponent
import com.bnorm.cocktailcalculator.data.RecipeResults
import com.bnorm.cocktailcalculator.data.Techniques
import com.bnorm.cocktailcalculator.lifecycleDestroy
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.itemSelections
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : LifecycleActivity() {

    private var recipeViewModel: RecipeViewModel by lifecycleDestroy()
    private var database: DatabaseReference by lifecycleDestroy()
    private var disposables: CompositeDisposable by lifecycleDestroy { it.dispose() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel::class.java)
        database = FirebaseDatabase.getInstance().reference
        disposables = CompositeDisposable()


        val ingredientAdapter = IngredientAdapter(applicationContext)
        ingredients.layoutManager = LinearLayoutManager(this);
        ingredients.adapter = ingredientAdapter

        val techniquesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Techniques.values().map(Techniques::str))
        techniquesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        technique.adapter = techniquesAdapter
        technique.setSelection(Techniques.Shaken.ordinal)


        disposables.add(ingredientAdapter.bind(recipeViewModel.ingredients))

        disposables.add(add_ingredient.clicks()
                .observeOn(Schedulers.io())
                .subscribe { recipeViewModel.addIngredient() })

        val uiUpdates = Observable.combineLatest<List<IngredientAmount>, Any, List<IngredientAmount>>(
                ingredientAdapter.ingredients,
                technique.itemSelections(),
                BiFunction { t, _ -> t })
        disposables.add(uiUpdates
                .observeOn(Schedulers.computation())
                .map {
                    val technique = Techniques.map[technique.selectedItem as String]!!
                    calcResults(it, technique) to technique.expected
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { (result, expected) ->
                            RecipeComponent.Dilution.set(expected, result, dilution_number, dilution_result)
                            RecipeComponent.Volume.set(expected, result, volume_number, volume_result)
                            RecipeComponent.Ethanol.set(expected, result, ethanol_number, ethanol_result)
                            RecipeComponent.Sugar.set(expected, result, sugar_number, sugar_result)
                            RecipeComponent.Acid.set(expected, result, acid_number, acid_result)
                        }))
    }

    private fun calcResults(ingredients: List<IngredientAmount>, technique: Techniques): RecipeResults {
        val valid = ingredients.filter { it.ingredient != null }

        // todo /100 is because database doesn't hold a true percent value
        val initialVolume = valid.map { it.amount }.sum()
        val initialEthanol = valid.map { it.amount * it.ingredient!!.ethanol }.sum() / initialVolume / 100.0
        val initialSugar = valid.map { it.amount * it.ingredient!!.sugar }.sum() / initialVolume
        val initialAcid = valid.map { it.amount * it.ingredient!!.acid }.sum() / initialVolume / 100.0

        val dilution = technique.dilution(initialEthanol)
        val volume = (dilution + 1.0) * initialVolume
        val ethanol = initialEthanol * initialVolume / volume
        val sugar = initialSugar * initialVolume / volume
        val acid = initialAcid * initialVolume / volume

        return RecipeResults(dilution, volume, ethanol, sugar, acid)
    }
}

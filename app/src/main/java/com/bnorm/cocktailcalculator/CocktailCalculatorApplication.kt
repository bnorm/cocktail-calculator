package com.bnorm.cocktailcalculator

import android.app.Application
import android.content.Context
import android.view.View
import com.bnorm.cocktailcalculator.data.db.ingredients
import com.bnorm.rx.firebase.singleValueEvent
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

val Context.app: CocktailCalculatorApplication get() = applicationContext as CocktailCalculatorApplication

val View.app: CocktailCalculatorApplication get() = context.app

class CocktailCalculatorApplication : Application() {

    lateinit var firebase: DatabaseReference

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        firebase = FirebaseDatabase.getInstance().reference

        firebase.child("ingredients")
                .singleValueEvent()
                .filter { !it.hasChildren() }
                .map {
                    val ref = it.ref
                    for ((_, ingredient) in ingredients()) {
                        ref.push().setValue(ingredient)
                    }
                }
                .ignoreElement()
                .subscribeOn(Schedulers.io())
                .subscribe()
    }
}

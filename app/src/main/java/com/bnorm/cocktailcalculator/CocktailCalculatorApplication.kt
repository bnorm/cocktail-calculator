package com.bnorm.cocktailcalculator

import android.app.Application
import android.content.Context
import android.view.View
import com.bnorm.cocktailcalculator.data.db.AppDatabase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

val Context.app: CocktailCalculatorApplication get() = applicationContext as CocktailCalculatorApplication

val View.app: CocktailCalculatorApplication get() = context.app

class CocktailCalculatorApplication : Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        db = AppDatabase.createPersistentDatabase(applicationContext)
        Completable.fromCallable { AppDatabase.init(db) }.subscribeOn(Schedulers.io()).subscribe()
    }
}

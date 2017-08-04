package com.bnorm.cocktailcalculator.data.db

import android.arch.persistence.room.*
import io.reactivex.Maybe
import io.reactivex.Single

// Note: Kotlin renames params, thus the awkward names
@Dao
abstract class IngredientDoa {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertOrUpdate(vararg ingredients: Ingredient)

    @Delete
    abstract fun delete(ingredient: Ingredient)

    @Query("SELECT * FROM ingredient")
    abstract fun findAll(): Single<List<Ingredient>>

    @Query("SELECT * FROM ingredient WHERE name = :p0")
    abstract fun findByName(name: String): Maybe<Ingredient>
}

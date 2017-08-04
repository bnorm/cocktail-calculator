package com.bnorm.cocktailcalculator.data.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "ingredient")
data class Ingredient(
        @PrimaryKey(autoGenerate = true) val id: Long,
        val name: String,
        val ethanol: Double,
        val sugar: Double,
        val acid: Double,
        val notes: String
)

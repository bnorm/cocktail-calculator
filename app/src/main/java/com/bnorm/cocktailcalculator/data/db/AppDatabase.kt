package com.bnorm.cocktailcalculator.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(
        entities = arrayOf(Ingredient::class),
        version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ingredients(): IngredientDoa

    companion object {
        private const val DB_NAME = "ingredient.db"

        fun createInMemoryDatabase(context: Context): AppDatabase
                = Room.inMemoryDatabaseBuilder(context.applicationContext, AppDatabase::class.java).build()

        fun createPersistentDatabase(context: Context): AppDatabase
                = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME).build()

        fun init(db: AppDatabase) {
            var i = 0L
            val ingredients = arrayOf(
                    Ingredient(++i, "Bourbon (80 proof)", 40.0, 0.0, 0.0, "Crème de cacao, honey, grapefruit, figs, apple, cognac, chamomile, rye, amontillado sherry"),
                    Ingredient(++i, "Bourbon (100 proof)", 50.0, 0.0, 0.0, "Crème de cacao, honey, grapefruit, figs, apple, cognac, chamomile, rye, amontillado sherry"),
                    Ingredient(++i, "Rye (80 proof)", 40.0, 0.0, 0.0, "Crème de cacao, honey, grapefruit, figs, apple, cognac, chamomile, bourbon, amontillado sherry"),
                    Ingredient(++i, "Rye (100 proof)", 50.0, 0.0, 0.0, "Crème de cacao, honey, grapefruit, figs, apple, cognac, chamomile, bourbon, amontillado sherry"),
                    Ingredient(++i, "Scotch (80 proof)", 40.0, 0.0, 0.0, "Apples, pears, cinnamon, oloroso sherry"),
                    Ingredient(++i, "Scotch (100 proof)", 50.0, 0.0, 0.0, "Apples, pears, cinnamon, oloroso sherry"),
                    Ingredient(++i, "Smoky Scotch (80 proof)", 40.0, 0.0, 0.0, "Pineapple, mint, lime"),
                    Ingredient(++i, "Smoky Scotch (100 proof)", 50.0, 0.0, 0.0, "Pineapple, mint, lime"),
                    Ingredient(++i, "Japanese Whisky (80 proof)", 40.0, 0.0, 0.0, "Pineapple, coconut"),
                    Ingredient(++i, "Japanese Whisky (100 proof)", 50.0, 0.0, 0.0, "Pineapple, coconut"),
                    Ingredient(++i, "Gin (80 proof)", 40.0, 0.0, 0.0, "Almost anything, herbs, vermouth, Chartreuse, honey, calvados, bitters, champagne, ginger, sherry"),
                    Ingredient(++i, "Gin (100 proof)", 50.0, 0.0, 0.0, "Almost anything, herbs, vermouth, Chartreuse, honey, calvados, bitters, champagne, ginger, sherry"),
                    Ingredient(++i, "Plymouth Gin", 41.2, 0.0, 0.0, "Almost anything, herbs, citrus, vermouth, Chartreuse, honey, calvados, bitters, champagne, ginger"),
                    Ingredient(++i, "Mezcal (80 proof)", 40.0, 0.0, 0.0, "Tequila (reposado), bell pepper, strawberry, Suze"),
                    Ingredient(++i, "Mezcal (100 proof)", 50.0, 0.0, 0.0, "Tequila (reposado), bell pepper, strawberry, Suze"),
                    Ingredient(++i, "Blanco Tequila (80 proof)", 40.0, 0.0, 0.0, "Grapefruit, black pepper, yellow Chartreuse, salt, jalapeño, Thai basil, kumquats, Aperol"),
                    Ingredient(++i, "Blanco Tequila (100 proof)", 50.0, 0.0, 0.0, "Grapefruit, black pepper, yellow Chartreuse, salt, jalapeño, Thai basil, kumquats, Aperol"),
                    Ingredient(++i, "Reposado Tequila (80 proof)", 40.0, 0.0, 0.0, "Grapefruit, black pepper, yellow Chartreuse, salt, jalapeño, Thai basil, kumquats, mezcal"),
                    Ingredient(++i, "Reposado Tequila (100 proof)", 50.0, 0.0, 0.0, "Grapefruit, black pepper, yellow Chartreuse, salt, jalapeño, Thai basil, kumquats, mezcal"),
                    Ingredient(++i, "Cachaça (80 proof)", 40.0, 0.0, 0.0, "Fruits and berries"),
                    Ingredient(++i, "Cachaça (100 proof)", 40.0, 0.0, 0.0, "Fruits and berries"),
                    Ingredient(++i, "Vodka (80 proof)", 40.0, 0.0, 0.0, "Anything with flavor"),
                    Ingredient(++i, "Vodka (100 proof)", 50.0, 0.0, 0.0, "Anything with flavor"),
                    Ingredient(++i, "Brandy (80 proof)", 40.0, 0.0, 0.0, "Calvados, sweet vermouth, honey, mint, cinnamon, apple, cherries, maple syrup, amontillado sherry"),
                    Ingredient(++i, "Brandy (100 proof)", 50.0, 0.0, 0.0, "Calvados, sweet vermouth, honey, mint, cinnamon, apple, cherries, maple syrup, amontillado sherry"),
                    Ingredient(++i, "Rum (80 proof)", 40.0, 0.0, 0.0, "Other rums, maple syrup, coffee, citrus (lime especially), herbs (mint, basic), ginger, curry leaf"),
                    Ingredient(++i, "Rum (100 proof)", 50.0, 0.0, 0.0, "Other rums, maple syrup, coffee, citrus (lime especially), herbs (mint, basic), ginger, curry leaf"),
                    Ingredient(++i, "Simple syrup", 0.0, 61.5, 0.0, "Combine 1 part sugar to 1 part water by weight"),
                    Ingredient(++i, "Rich simple syrup", 0.0, 80.0, 0.0, "Combine 2 parts sugar to 1 part water by weight"),
                    Ingredient(++i, "Agave syrup", 0.0, 61.5, 0.0, "Add 50g of water for each 100g of agave nectar"),
                    Ingredient(++i, "Honey syrup", 0.0, 61.5, 0.0, "Add 64g of water for each 100g of honey"),
                    Ingredient(++i, "Demerara syrup", 0.0, 61.5, 0.0, "Combine 1 part demarara sugar to 1 part water by weight"),
                    Ingredient(++i, "Maple syrup", 0.0, 87.5, 0.0, "Use \"Grade A: Dark Color & Robust Taste\" syrup"),
                    Ingredient(++i, "Grenadine", 0.0, 66.6, 0.0, "Sweetness is from Rose's Grenadine"),
                    Ingredient(++i, "Commercial orgeat", 0.0, 85.5, 0.0, "Pairs well with pears"),
                    Ingredient(++i, "Any nut orgeat", 0.0, 61.5, 0.0, "Pairs well with pears"),
                    Ingredient(++i, "Lemon juice", 0.0, 1.6, 6.0, "Pairs with almost everything"),
                    Ingredient(++i, "Lime juice", 0.0, 1.6, 6.0, "Pairs with almost everything"),
                    Ingredient(++i, "Grapefruit juice", 0.0, 10.4, 2.4, "Tequila, bourbon, rye, cinnamon, Aperol"),
                    Ingredient(++i, "Orange juice", 0.0, 12.4, 0.8, "Gin"),
                    Ingredient(++i, "Strawberry", 0.0, 8.0, 1.5, "Black pepper, cinnamon"),
                    Ingredient(++i, "Concord Grape", 0.0, 18.0, 0.5, ""),
                    Ingredient(++i, "Cranberry", 0.0, 13.3, 3.6, ""),
                    Ingredient(++i, "Granny Smith apple", 0.0, 13.0, 0.9, "Celery"),
                    Ingredient(++i, "Honeycrisp apple", 0.0, 13.8, 0.7, "Celery"),
                    Ingredient(++i, "Angostura bitters", 44.7, 4.2, 0.0, "1 dash = 0.8ml or .027oz"),
                    Ingredient(++i, "Peychauds bitters", 35.0, 5.0, 0.0, "1 dash = 0.8ml or .027oz"),
                    Ingredient(++i, "Carpano Antica sweet vermouth", 16.5, 16.0, 0.6, "Coffee, bourbon, rye"),
                    Ingredient(++i, "Dolin Blanc vermouth", 16.0, 13.0, 0.6, "Chamomile, watermelon, sage"),
                    Ingredient(++i, "Dolin Dry vermouth", 17.5, 3.0, 0.6, "Peach, apricot"),
                    Ingredient(++i, "Dolin Rouge sweet vermouth", 16.0, 13.0, 0.6, "Chai tea"),
                    Ingredient(++i, "Generic dry vermouth", 17.5, 3.0, 0.6, "Peach, apricot"),
                    Ingredient(++i, "Generic sweet vermouth", 16.5, 16.0, 0.6, "Chai tea"),
                    Ingredient(++i, "Lillet Blanc", 17.0, 9.5, 0.6, "Dry sherry, Cointreau"),
                    Ingredient(++i, "Amaro CioCiaro", 30.0, 16.0, 0.0, "Peach"),
                    Ingredient(++i, "Amer Picon", 15.0, 20.0, 0.0, "Maraschino liqueur, whiskey"),
                    Ingredient(++i, "Aperol", 11.0, 24.0, 0.0, "Blanco tequila, sherry, mango, grapefruit"),
                    Ingredient(++i, "Benedictine", 40.0, 24.5, 0.0, "Sweet vermouth, whiskey, brandy"),
                    Ingredient(++i, "Campari", 24.0, 24.0, 0.0, "Chocolate, raspberries, sloe gin"),
                    Ingredient(++i, "Chartreuse, Green", 55.0, 25.0, 0.0, "Coffee, chocolate"),
                    Ingredient(++i, "Chartreuse, Yellow", 40.0, 31.2, 0.0, "Strawberry, tequila"),
                    Ingredient(++i, "Cointreau (triple sec)", 40.0, 25.0, 0.0, "Gin, cherry, brandy, Lillet Blanc, Crème de violette"),
                    Ingredient(++i, "Crème de cacao, white", 24.0, 39.5, 0.0, "Bourbon, rye, gin, Lillet Blanc"),
                    Ingredient(++i, "Crème de violette", 20.0, 37.5, 0.0, "Gin, Cointreau, Maraschino liqueur"),
                    Ingredient(++i, "Drambuie", 40.0, 30.0, 0.0, ""),
                    Ingredient(++i, "Fernet Branca", 39.0, 8.0, 0.0, "Gin, sweet vermouth, whiskey, cognac"),
                    Ingredient(++i, "Luxardo Maraschino", 32.0, 35.0, 0.0, "Crème de violette, whiskey, gin, sweet vermouth, green Chartreuse, pineapple"),
                    Ingredient(++i, "Tonic water (Schweppes)", 0.0, 9.0, 0.0, "Gin"),
                    Ingredient(++i, "Soda water / still water", 0.0, 0.0, 0.0, ""),
                    Ingredient(++i, "Tonic water (Fever Tree)", 0.0, 8.0, 0.0, "Gin"),
                    Ingredient(++i, "Egg white", 0.0, 0.0, 0.0, "1 large egg white is ~1oz (30ml)"),
                    Ingredient(++i, "Cabernet sauvignon", 14.5, 0.2, 0.6, ""),
                    Ingredient(++i, "Coconut water", 0.0, 6.0, 0.0, "Japanese whisky"),
                    Ingredient(++i, "Espresso", 0.0, 0.0, 1.5, "Rum, Carpano Antica, green Chartreuse")
            )

            val doa = db.ingredients()
            for (ingredient in ingredients) {
                doa.insertOrUpdate(ingredient)
            }
        }
    }
}

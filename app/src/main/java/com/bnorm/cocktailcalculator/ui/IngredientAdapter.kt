package com.bnorm.cocktailcalculator.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bnorm.cocktailcalculator.R
import com.bnorm.cocktailcalculator.app
import com.bnorm.cocktailcalculator.data.db.AppDatabase
import com.bnorm.cocktailcalculator.data.db.Ingredient
import com.jakewharton.rxbinding2.widget.AdapterViewItemClickEvent
import com.jakewharton.rxbinding2.widget.itemClickEvents
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.ingredient.view.*
import kotlin.properties.Delegates

class IngredientAdapter(
        private val context: Context
) : ObservingAdapter<IngredientAmount, IngredientAmountViewHolder>(IngredientAmount::id) {

    override var data: List<IngredientAmount> = emptyList()
        set(value) {
            field = value
            subject.onNext(field)
        }

    private val dbIngredients = context.app.db.ingredients().findAll()
            .subscribeOn(Schedulers.io())
            .map { it.map { it.name } }
            .cache()

    private val subject: Subject<List<IngredientAmount>> = BehaviorSubject.createDefault(data.toList())
    val ingredients: Observable<List<IngredientAmount>> get() = subject

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientAmountViewHolder {
        val holder = IngredientAmountViewHolder(context, parent, dbIngredients)
        holder.updates.subscribe {
            subject.onNext(data)
        }
        return holder
    }
}

class IngredientAmountViewHolder(
        context: Context,
        parent: ViewGroup,
        ingredients: Single<List<String>>
) : ObservingAdapter.ViewHolder<IngredientAmount>(LayoutInflater.from(context).inflate(R.layout.ingredient, parent, false)) {

    private val db: AppDatabase = context.app.db
    private var ingredientAmount by Delegates.notNull<IngredientAmount>()

    private val selectedIngredient = itemView.spinner.itemClickEvents()
            .flatMapMaybe {
                db.ingredients().findByName(it.item as String)
                        .subscribeOn(Schedulers.io())
            }
            .doOnNext { ingredientAmount.ingredient = it }
            .publish()
            .autoConnect()

    private val amountChanges = itemView.textInputEditText.textChanges()
            .filter { it.isNotEmpty() && it.toString() != "." }
            .map { it.toString().toDouble() }
            .doOnNext { ingredientAmount.amount = it }
            .publish()
            .autoConnect()

    val updates: Observable<Unit> = Observable.merge(selectedIngredient, amountChanges).map {}

    init {
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemView.spinner.setAdapter(adapter)
        ingredients.subscribe { it -> adapter.addAll(it) }

        selectedIngredient
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { set(it) }

        itemView.spinner.textChanges()
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { clear() }
    }

    override fun bind(item: IngredientAmount) {
        this.ingredientAmount = item

        update(item.ingredient)
        if (item.ingredient == null) {
            itemView.spinner.setText("")
        }
        itemView.textInputEditText.setText(item.amount.toString())
    }

    private fun update(it: Ingredient?) {
        if (it != null) {
            set(it)
        } else {
            clear()
        }
    }

    private fun set(it: Ingredient) {
        itemView.spinner.setText(it.name, true)
        itemView.textView31.text = "Ethanol: ${it.ethanol}"
        itemView.textView41.text = "Sugar: ${it.sugar}"
        itemView.textView5.text = "Acid: ${it.acid}"
    }

    private fun clear() {
        itemView.textView31.text = "Ethanol: --"
        itemView.textView41.text = "Sugar: --"
        itemView.textView5.text = "Acid: --"
    }
}

private val AdapterViewItemClickEvent.item: Any
    get() = view().getItemAtPosition(position())

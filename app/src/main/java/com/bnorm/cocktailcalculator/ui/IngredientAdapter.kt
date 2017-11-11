package com.bnorm.cocktailcalculator.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bnorm.cocktailcalculator.R
import com.bnorm.cocktailcalculator.app
import com.bnorm.cocktailcalculator.data.model.Ingredient
import com.bnorm.rx.firebase.child
import com.bnorm.rx.firebase.children
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

    private val dbIngredients = context.app.firebase.child("ingredients")
            .children<Ingredient>()
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
        private val context: Context,
        parent: ViewGroup,
        ingredients: Single<List<String>>
) : ObservingAdapter.ViewHolder<IngredientAmount>(
        LayoutInflater.from(context).inflate(R.layout.ingredient, parent, false)
) {

    private val firebase = context.app.firebase
    private var ingredientAmount by Delegates.notNull<IngredientAmount>()

    private val selectedIngredient = itemView.ingredient.itemClickEvents()
            .flatMapMaybe {
                firebase.child("ingredients")
                        .orderByChild("name")
                        .equalTo(it.item as String)
                        .child<Ingredient>()
                        .subscribeOn(Schedulers.io())
            }
            .doOnNext { ingredientAmount.ingredient = it }
            .publish()
            .autoConnect()

    private val amountChanges = itemView.amount.textChanges()
            .filter { it.isNotEmpty() && it.toString() != "." }
            .map { it.toString().toDouble() }
            .doOnNext { ingredientAmount.amount = it }
            .publish()
            .autoConnect()

    val updates: Observable<IngredientAmount> = Observable.merge(selectedIngredient, amountChanges)
            .map { ingredientAmount }

    init {
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemView.ingredient.setAdapter(adapter)
        ingredients.subscribe { it -> adapter.addAll(it) }

        selectedIngredient
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { set(it) }

        itemView.ingredient.textChanges()
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { clear() }
    }

    override fun bind(item: IngredientAmount) {
        this.ingredientAmount = item

        update(item.ingredient)
        itemView.ingredient.setText(item.ingredient?.name ?: "")
        itemView.amount.setText(item.amount.toString())
    }

    private fun update(it: Ingredient?) {
        if (it != null) {
            set(it)
        } else {
            clear()
        }
    }

    private fun set(ingredient: Ingredient) {
        itemView.ethanol.text = context.getString(R.string.ethanol_display, ingredient.ethanol.toString())
        itemView.sugar.text = context.getString(R.string.sugar_display, ingredient.sugar.toString())
        itemView.acid.text = context.getString(R.string.acid_display, ingredient.acid.toString())
    }

    private fun clear() {
        itemView.ethanol.text = context.getString(R.string.ethanol_display, "--")
        itemView.sugar.text = context.getString(R.string.sugar_display, "--")
        itemView.acid.text = context.getString(R.string.acid_display, "--")
    }
}

private val AdapterViewItemClickEvent.item: Any
    get() = view().getItemAtPosition(position())

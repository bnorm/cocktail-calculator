package com.bnorm.cocktailcalculator.ui

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

abstract class ObservingAdapter<T, VH : ObservingAdapter.ViewHolder<T>>(
        private val id: T.() -> Long
) : RecyclerView.Adapter<VH>() {

    protected abstract var data: List<T>

    override fun getItemCount() = data.size

    override fun getItemId(position: Int) = data[position].id()

    fun bind(source: Observable<List<T>>): Disposable {
        data class DataWithDiff<out E>(val data: List<E>, val diff: DiffUtil.DiffResult?)

        return source
                .scan(DataWithDiff(emptyList<T>(), null)) { (old, _), new ->
                    DataWithDiff(new, DiffUtil.calculateDiff(DiffCallback(old, new, id)))
                }
                .doOnNext { data = it.data }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.diff != null) {
                        it.diff.dispatchUpdatesTo(this)
                    } else {
                        notifyDataSetChanged()
                    }
                }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(data[position])
    }

    abstract class ViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }
}


private class DiffCallback<T>(private val old: List<T>,
                              private val new: List<T>,
                              private val id: T.() -> Long)
    : DiffUtil.Callback() {
    override fun getOldListSize() = old.size
    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int)
            = old[oldItemPosition].id() == new[newItemPosition].id()

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int)
            = old[oldItemPosition] == new[newItemPosition]
}

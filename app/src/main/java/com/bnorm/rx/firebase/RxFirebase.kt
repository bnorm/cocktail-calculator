package com.bnorm.rx.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

fun Query.valueEvents(): Observable<DataSnapshot> {
    return Observable.create<DataSnapshot> { emitter ->
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                emitter.onNext(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                emitter.onError(error.toException())
            }
        }

        addValueEventListener(listener)
        emitter.setCancellable { removeEventListener(listener) }
    }
}

fun Query.singleValueEvent(): Single<DataSnapshot> {
    return Single.create<DataSnapshot> { emitter ->
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                emitter.onSuccess(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                emitter.onError(error.toException())
            }
        }

        addListenerForSingleValueEvent(listener)
        emitter.setCancellable { removeEventListener(listener) }
    }
}

inline fun <reified T : Any> Query.children(): Single<List<T>> {
    return singleValueEvent()
            .map {
                it.children
                        .map { it.getValue(T::class.java) }
                        .filterNotNull()
            }
}

inline fun <reified T : Any> Query.child(): Maybe<T> {
    return children<T>()
            .flatMapObservable { Observable.fromIterable(it) }
            .singleElement()
}

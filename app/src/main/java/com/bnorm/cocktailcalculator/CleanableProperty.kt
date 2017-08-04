package com.bnorm.cocktailcalculator

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class CleanableProperty<T : Any>(private val cleaner: (T) -> Unit) : ReadWriteProperty<Any?, T> {

    private var value: T? = null

    fun clean() {
        // only clean if initialized
        value?.let { cleaner(it) }
        value = null
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("Property ${property.name} is not initialized.")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

fun <T : Any> LifecycleOwner.lifecycleDestroy(destroy: (T) -> Unit = {}): CleanableProperty<T> {
    return lifecycleDestroy(lifecycle, destroy)
}

fun <T : Any> lifecycleDestroy(lifecycle: Lifecycle, destroy: (T) -> Unit = {}): CleanableProperty<T> {
    val property = CleanableProperty<T>(destroy)
    lifecycle.addObserver(LifecycleCleanableProperty(property))
    return property
}

private class LifecycleCleanableProperty(
        private val property: CleanableProperty<*>
) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        property.clean()
    }
}

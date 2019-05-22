package tk.jonathancowling.inventorytracker.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class Resettable<T>(val reset: ()->T, val shouldReset: (T)->Boolean) : ReadWriteProperty<Any, T> {
    private var value = reset()
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (shouldReset(value)) {
            value = reset()
        }
        return value
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.value = value
    }
}
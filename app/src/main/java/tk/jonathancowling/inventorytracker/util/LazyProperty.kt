package tk.jonathancowling.inventorytracker.util

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <R, T> lazyReadOnlyProperty(initProperty: () -> ReadOnlyProperty<R, T>) =
    object : ReadOnlyProperty<R, T> {
        private lateinit var underlyingProperty : ReadOnlyProperty<R, T>

        override fun getValue(thisRef: R, property: KProperty<*>): T {
            if (!this::underlyingProperty.isInitialized) {
                underlyingProperty = initProperty()
            }
            return underlyingProperty.getValue(thisRef, property)
        }

    }

fun <R, T> lazyReadWriteProperty(initProperty: () -> ReadWriteProperty<R, T>) =
    object : ReadWriteProperty<R,T> {

        lateinit var underlyingProperty: ReadWriteProperty<R, T>

        override fun getValue(thisRef: R, property: KProperty<*>): T {
            if (!this::underlyingProperty.isInitialized) {
                underlyingProperty = initProperty()
            }
            return underlyingProperty.getValue(thisRef, property)
        }

        override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
            if (!this::underlyingProperty.isInitialized) {
                underlyingProperty = initProperty()
            }
            underlyingProperty.setValue(thisRef, property, value)
        }
    }
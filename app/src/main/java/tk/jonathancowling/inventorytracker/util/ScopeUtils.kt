package tk.jonathancowling.inventorytracker.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.get
import tk.jonathancowling.inventorytracker.ScopeViewModel
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

data class KeyedScope(val key: Int, val store: ViewModelStore)

fun existingScopeKey(ignoreError: Boolean = false) = object : ReadOnlyProperty<Fragment, Int> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>) =
        thisRef.arguments?.getInt(property.name)?: let {
            if (ignoreError) {
                throw NullPointerException()
            }
            ScopeViewModel.GLOBAL
        }
}

fun existingScope(ignoreError: Boolean = false) = object : ReadOnlyProperty<Fragment, KeyedScope> {

    val before = existingScopeKey(ignoreError)

    override fun getValue(thisRef: Fragment, property: KProperty<*>): KeyedScope =
        before.getValue(thisRef, property).let {
            val vm = ViewModelProviders.of(thisRef.requireActivity()).get<ScopeViewModel>()

            KeyedScope(it, vm[it, it]!!)
        }
}


private fun pushToParent(parentScope: Int, activity: FragmentActivity) =
    ViewModelProviders.of(activity)
        .get(ScopeViewModel::class.java)
        .push(parentScope)


fun newScopeKey(parentScope: Int = ScopeViewModel.GLOBAL) = object : ReadOnlyProperty<Fragment, Int> {

    val before = newScope(parentScope)

    override fun getValue(thisRef: Fragment, property: KProperty<*>) =
        before.getValue(thisRef, property).key
}

fun newScope(parentScope: Int = ScopeViewModel.GLOBAL) = object : ReadOnlyProperty<Fragment, KeyedScope> {

    private lateinit var value: KeyedScope

    override fun getValue(thisRef: Fragment, property: KProperty<*>): KeyedScope {
        if (!::value.isInitialized) {
            value = pushToParent(parentScope, thisRef.requireActivity()).let {
                KeyedScope(it.first, it.second)
            }
        }

        return value
    }
}
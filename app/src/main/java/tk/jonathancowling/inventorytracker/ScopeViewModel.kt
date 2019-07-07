package tk.jonathancowling.inventorytracker

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import tk.jonathancowling.inventorytracker.util.Tree

class ScopeViewModel : ViewModel() {

    @SuppressLint("UseSparseArrays")
    private val map = HashMap<Int, Tree<Int, ViewModelStore>>()
        .apply {
            this[GLOBAL] = Tree.root(Pair(GLOBAL, ViewModelStore()))
        }

    fun global() = map[GLOBAL]!![GLOBAL]!!

    operator fun get(from: Int, target: Int = from) = map[from]?.get(target)

    fun push(from: Int = GLOBAL): Pair<Int, ViewModelStore> {
        val store = ViewModelStore()
        val key = map.size
        val tree = Tree.of(map[from] ?: map[GLOBAL]!!, Pair(key, store))
        map[key] = tree
        return Pair(key, store)
    }

    fun pop(key: Int) {
        map[key]!!.pop()
        map.remove(key)
    }

    companion object {
        const val GLOBAL = 0
    }
}
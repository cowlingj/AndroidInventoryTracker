package tk.jonathancowling.inventorytracker

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import tk.jonathancowling.inventorytracker.util.KeyedTree

class ScopeViewModel : ViewModel() {

    @SuppressLint("UseSparseArrays")
    private val map = HashMap<Int, KeyedTree<Int, ViewModelStore>>()
        .apply {
            this[GLOBAL] = KeyedTree.root(GLOBAL, ViewModelStore())
        }

    fun global() = map[GLOBAL]!![GLOBAL]!!

    operator fun get(from: Int, target: Int = from) = map[from]?.get(target)

    fun push(from: Int = GLOBAL): Pair<Int, ViewModelStore> {
        val store = ViewModelStore()
        val key = map.size
        val tree = KeyedTree.of(map[from] ?: map[GLOBAL]!!, key, store)
        map[key] = tree
        return Pair(key, store)
    }

    private fun selectNodesInTree(key: Int): List<Int> {
        val idsToRemove = mutableListOf(key)

        map[key]!!.children.forEach {
            idsToRemove += selectNodesInTree(it.key)
        }

        return idsToRemove
    }

    fun pop(key: Int) {
        selectNodesInTree(key).forEach {
            map[it]!!.apply {
                this.children.clear()
            }
            map - it
        }
    }

    fun popChildren(key: Int) {
        map[key]!!.children.forEach { pop(it.key) }
    }

    companion object {
        const val GLOBAL = 0
    }
}
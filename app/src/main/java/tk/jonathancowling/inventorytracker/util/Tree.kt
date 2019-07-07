package tk.jonathancowling.inventorytracker.util

class Tree<K, V> private constructor(
    private val parent: Tree<K, V>?,
    private val keyedData: Pair<K, V>
) {

    private val children = mutableListOf<Tree<K, V>>()

    operator fun get(key: K): V? {

        if (keyedData.first == key) {
            return keyedData.second
        }

        return parent?.get(key)
    }

    private fun push(tree: Tree<K, V>) {
        children += tree
    }

    fun pop() {
        parent?.children?.minusAssign(this)
    }

    companion object {
        fun <K, V> of(parent: Tree<K, V>, keyedData: Pair<K, V>) = Tree(parent, keyedData)
            .apply {
                parent.push(this)
            }
        fun <K, V> root(keyedData: Pair<K, V>) = Tree(null, keyedData)
    }
}
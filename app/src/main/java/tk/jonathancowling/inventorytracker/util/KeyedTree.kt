package tk.jonathancowling.inventorytracker.util

class KeyedTree<K, V> private constructor(
    private val parent: KeyedTree<K, V>?,
    val key: K,
    val data: V
) {

    val children = mutableListOf<KeyedTree<K, V>>()

    operator fun get(key: K): V? {

        if (this.key == key) {
            return data
        }

        return parent?.get(key)
    }

    private fun push(tree: KeyedTree<K, V>) {
        children += tree
    }

    fun pop() {
        parent?.children?.minusAssign(this)
    }

    companion object {
        fun <K, V> of(parent: KeyedTree<K, V>, key: K, data: V) = KeyedTree(parent, key, data)
            .apply {
                parent.push(this)
            }
        fun <K, V> root(key: K, data: V) = KeyedTree(null, key, data)
    }
}
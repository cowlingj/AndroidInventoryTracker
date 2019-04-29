package tk.jonathancowling.inventorytracker.listclient

import java.util.Objects

open class Item(val id: String, val name: String, val quantity: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other == null || javaClass != other.javaClass) {
            super.equals(other)
        } else hashCode() == other.hashCode()
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name, quantity)
    }
}


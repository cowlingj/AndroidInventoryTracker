package tk.jonathancowling.inventorytracker.listclient.models

import java.util.Objects

class ItemPrototype(val name: String, val quantity: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return if (other == null || javaClass != other.javaClass) {
            super.equals(other)
        } else hashCode() == other.hashCode()
    }

    override fun hashCode(): Int {
        return Objects.hash(name, quantity)
    }
}


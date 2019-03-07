package tk.jonathancowling.inventorytracker.settings

class Model {
    val data = listOf<Item>()

    data class Item(val name: String)
}
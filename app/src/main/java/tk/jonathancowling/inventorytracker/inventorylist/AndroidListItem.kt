package tk.jonathancowling.inventorytracker.inventorylist

import tk.jonathancowling.inventorytracker.clients.list.models.Item

class AndroidListItem(item: Item, var isInUse: Boolean = true) : Item(item.id, item.name, item.quantity) {
    constructor(id: String, name: String, quantity: Int, isInUse: Boolean = true)
            : this(Item(id, name, quantity), isInUse)
}
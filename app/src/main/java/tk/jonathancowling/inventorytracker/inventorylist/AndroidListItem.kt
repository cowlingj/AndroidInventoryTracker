package tk.jonathancowling.inventorytracker.inventorylist

class AndroidListItem(private val item: ListItem, var isInUse: Boolean = true) : ListItem by item {
    constructor(id: Int, name: String, quantity: Int, isInUse: Boolean = true)
            : this(ListItemImpl(id, name, quantity), isInUse)
}
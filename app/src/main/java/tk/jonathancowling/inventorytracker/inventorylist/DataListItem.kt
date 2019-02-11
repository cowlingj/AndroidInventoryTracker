package tk.jonathancowling.inventorytracker.inventorylist

class DataListItem(override val id: Int, override val name: String, override val quantity: Int, var isInUse: Boolean):
    ListItem(id, name, quantity)
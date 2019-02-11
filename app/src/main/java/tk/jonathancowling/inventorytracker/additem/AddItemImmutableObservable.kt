package tk.jonathancowling.inventorytracker.additem

interface AddItemImmutableObservable {
    fun getName(): String
    fun getQuantity(): Int?
}
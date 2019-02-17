package tk.jonathancowling.inventorytracker.additem

import tk.jonathancowling.inventorytracker.util.TripleState

data class Model(val name: String, val quantity: TripleState<Int>)
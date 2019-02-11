package tk.jonathancowling.inventorytracker.util

fun <E> List<E>.splice(start: Int, deleteCount: Int, spliceIn: List<E>): List<E>
        = this.subList(0, start) + spliceIn + this.subList(start + deleteCount, this.size)
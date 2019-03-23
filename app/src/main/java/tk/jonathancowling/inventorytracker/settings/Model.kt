package tk.jonathancowling.inventorytracker.settings

import java.net.URL

class Model {
    val data = listOf<Item>()

    data class Item(val inventoryEndpoint: Endpoint)
    data class Endpoint(val url: URL, val apiKey: String)
}
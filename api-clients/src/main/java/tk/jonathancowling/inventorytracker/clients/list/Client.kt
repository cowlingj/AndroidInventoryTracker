package tk.jonathancowling.inventorytracker.clients.list

import tk.jonathancowling.BuildConfig

class Client(baseUrl: String, apiKey: String) : ApiClient(BuildConfig.API_KEY_NAME, apiKey) {
    init {
        super.getAdapterBuilder().baseUrl(baseUrl)
    }
}
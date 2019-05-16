package tk.jonathancowling.inventorytracker.clients.list

import tk.jonathancowling.BuildConfig
import tk.jonathancowling.inventorytracker.clients.list.ApiClient

class Client : ApiClient(BuildConfig.API_KEY_NAME, BuildConfig.API_KEY_VALUE) {
    init {
        super.getAdapterBuilder().baseUrl(BuildConfig.BASE_URL)
    }
}
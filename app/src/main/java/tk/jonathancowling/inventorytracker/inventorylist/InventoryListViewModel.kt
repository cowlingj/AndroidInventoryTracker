package tk.jonathancowling.inventorytracker.inventorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import tk.jonathancowling.inventorytracker.clients.list.models.Item
import tk.jonathancowling.inventorytracker.inventorylist.services.ApiInventoryListService
import tk.jonathancowling.inventorytracker.inventorylist.services.EphemeralInventoryListService
import tk.jonathancowling.inventorytracker.inventorylist.services.InventoryListService
import tk.jonathancowling.inventorytracker.inventorylist.services.InventoryListServiceProvider
import tk.jonathancowling.inventorytracker.settings.SettingsRepository

class InventoryListViewModel private constructor(private val listServiceProvider: () -> InventoryListService) : ViewModel() {

    fun getErrors(): LiveData<out Throwable> = listServiceProvider().errors

    fun getData(): LiveData<out PagedList<Item>> = listServiceProvider().inventoryList

    fun addItem(name: String, quantity: Int) = listServiceProvider().add(name, quantity)

    fun removeItem(id: String) = listServiceProvider().remove(id)

    class Factory(private val settingsRepository: SettingsRepository) : ViewModelProvider.Factory {

        private val ephemeralService = EphemeralInventoryListService()

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            val provider = InventoryListServiceProvider(settingsRepository, mapOf(
                InventoryListServiceProvider.Choice.API to { it ->
                    ApiInventoryListService(it.baseUrl!!, it.apiKey!!)
                },
                InventoryListServiceProvider.Choice.EPHEMERAL to { _ ->
                    ephemeralService
                }
            )) { ephemeralService }

            @Suppress("UNCHECKED_CAST")
            return InventoryListViewModel(provider) as T
        }

    }

}
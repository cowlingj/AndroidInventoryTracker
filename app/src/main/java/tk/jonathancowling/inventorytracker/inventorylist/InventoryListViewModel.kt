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
import javax.inject.Provider

class InventoryListViewModel private constructor(private val listServiceProvider: Provider<InventoryListService>) : ViewModel() {

    fun getErrors(): LiveData<out Throwable> = listServiceProvider.get().errors

    fun getData(): LiveData<out PagedList<Item>> = listServiceProvider.get().inventoryList

    fun addItem(name: String, quantity: Int) = listServiceProvider.get().add(name, quantity)

    fun removeItem(id: String) = listServiceProvider.get().remove(id)

    class Factory(private val settingsRepository: SettingsRepository) : ViewModelProvider.Factory {

        private val ephemeralService = EphemeralInventoryListService()

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            val provider = InventoryListServiceProvider(settingsRepository, mapOf(
                InventoryListServiceProvider.Choice.API to { it ->
                    ApiInventoryListService(it.baseUrl!!, it.apiKey!!)
                },
                InventoryListServiceProvider.Choice.EPHEMERAL to { it ->
                    ephemeralService
                }
            )) { ephemeralService }

            @Suppress("UNCHECKED_CAST")
            return InventoryListViewModel(provider) as T
        }

    }

}
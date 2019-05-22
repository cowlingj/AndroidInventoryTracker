package tk.jonathancowling.inventorytracker.inventorylist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import tk.jonathancowling.inventorytracker.DaggerAndroidComponent
import tk.jonathancowling.inventorytracker.authentication.services.DaggerAuthComponent
import tk.jonathancowling.inventorytracker.clients.list.models.Item
import tk.jonathancowling.inventorytracker.inventorylist.services.DaggerInventoryListComponent
import tk.jonathancowling.inventorytracker.inventorylist.services.InventoryListService
import tk.jonathancowling.inventorytracker.settings.DaggerSettingsComponent
import javax.inject.Provider

class InventoryListViewModel private constructor(private val listServiceProvider: Provider<InventoryListService>) : ViewModel() {

    fun getErrors(): LiveData<out Throwable> = listServiceProvider.get().errors

    fun getData(): LiveData<out PagedList<Item>> = listServiceProvider.get().inventoryList

    fun addItem(name: String, quantity: Int) = listServiceProvider.get().add(name, quantity)

    fun removeItem(id: String) = listServiceProvider.get().remove(id)

    class Factory(private val ctx: Context): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val android = DaggerAndroidComponent
                    .builder()
                    .context(ctx)
                    .build()

                val auth = DaggerAuthComponent
                    .create()

                val settings = DaggerSettingsComponent
                    .builder()
                    .androidComponent(android)
                    .authComponent(auth)
                    .build()

                val list = DaggerInventoryListComponent
                    .builder()
                    .settingsComponent(settings)
                    .build()

            @Suppress("UNCHECKED_CAST")
            return InventoryListViewModel(list.service()) as T
        }

    }

}
package tk.jonathancowling.inventorytracker.inventorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.ObservableSource
import io.reactivex.Single
import tk.jonathancowling.inventorytracker.BuildConfig
import tk.jonathancowling.inventorytracker.listclient.ApiClient
import tk.jonathancowling.inventorytracker.listclient.api.DefaultApi
import tk.jonathancowling.inventorytracker.listclient.Item
import tk.jonathancowling.inventorytracker.listclient.ItemPrototype
import tk.jonathancowling.inventorytracker.listclient.PartialItem
import tk.jonathancowling.inventorytracker.util.mapIndexedToPairs
import tk.jonathancowling.inventorytracker.util.splice

class ApiInventoryListService : InventoryListService {

    private val listClient = ApiClient().setApiKey(BuildConfig.API_KEY).apply {
        this.adapterBuilder.baseUrl(BuildConfig.API_KEY)
    }.createService(DefaultApi::class.java)

    private val internal: MutableLiveData<out List<Item>> = MutableLiveData()
    override val inventoryList: LiveData<out List<Item>> = internal

    override fun add(name: String, quantity: Int): Single<out Item> {
        return listClient.listPost(ItemPrototype(name, quantity)).map {
            internal.value = listOf(it) + internal.value.orEmpty()
            it
        }.singleOrError()
    }

    override fun remove(id: String): Single<out Item> {
        return listClient.listDelete(id).singleOrError().doOnSuccess{
            internal.value = internal.value.orEmpty() - it
        }
    }

    override fun find(id: String): Single<out Item> {
        TODO()
    }

    override fun pager(pageSize: Int): ObservableSource<Unit> {
        return ObservableSource { ob ->
            listClient.listGet(null, null)
                .singleOrError()
                .doOnSuccess { internal.value = it }
                .subscribe({
                    ob.onNext(Unit)
                    ob.onComplete()
                }, {
                    ob.onError(it)
                })
        }
    }

    override fun all(): Single<Unit> {
        return listClient.listGet(null, null).map {
            internal.value = it
            Unit
        }.singleOrError()
    }

    override fun changeName(id: String, newName: String): Single<out Item> {
        return listClient.listPut(PartialItem(id, newName, null)).singleOrError()
            .map { item ->
                val i = internal
                    .value
                    .orEmpty()
                    .mapIndexedToPairs()
                    .single { it.second.id == id }
                    .first
                internal.value = internal.value.orEmpty().splice(i, 1, listOf(item))
                item
            }
    }

    override fun changeQuantity(id: String, newQuantity: Int): Single<out Item> {
        return listClient.listPut(PartialItem(id, null, newQuantity)).singleOrError()
            .doOnSuccess { item ->
                val i = internal
                    .value
                    .orEmpty()
                    .mapIndexedToPairs()
                    .single { it.second.id == id }
                    .first
                internal.value = internal.value.orEmpty().splice(i, 1, listOf(item))
            }
    }
}
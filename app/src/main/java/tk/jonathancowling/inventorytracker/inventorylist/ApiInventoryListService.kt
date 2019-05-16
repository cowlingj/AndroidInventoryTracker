package tk.jonathancowling.inventorytracker.inventorylist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import tk.jonathancowling.inventorytracker.clients.list.Client
import tk.jonathancowling.inventorytracker.clients.list.api.DefaultApi
import tk.jonathancowling.inventorytracker.clients.list.models.Item
import tk.jonathancowling.inventorytracker.clients.list.models.ItemPrototype
import tk.jonathancowling.inventorytracker.clients.list.models.PartialItem
import tk.jonathancowling.inventorytracker.util.AutoDisposable
import tk.jonathancowling.inventorytracker.util.rx.ApiCallConfig
import java.io.IOException

class ApiInventoryListService : InventoryListService {

    private val listClient = Client().createService(DefaultApi::class.java)
    private val apiCallConfig = ApiCallConfig()

    override val errors = MutableLiveData<Throwable>()

    private lateinit var dataSrc: InventoryListDataSource

    inner class InventoryListDataSource : PageKeyedDataSource<String, Item>() {

        private val disposable: CompositeDisposable by AutoDisposable.Composite()

        override fun addInvalidatedCallback(onInvalidatedCallback: InvalidatedCallback) {
            disposable.clear()
            super.addInvalidatedCallback(onInvalidatedCallback)
        }

        override fun loadInitial(
            params: LoadInitialParams<String>,
            callback: LoadInitialCallback<String, Item>
        ) {
            disposable.add(get(null, params.requestedLoadSize)
                .subscribe({
                    callback.onResult(it, null, null)
                },{
                    Log.w("API", "failed to get initial list ${it.message}")
                    errors.value = IOException()
                    callback.onResult(emptyList(), null, null)
                }))
        }

        override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Item>) {
            disposable.add(get(params.key, params.requestedLoadSize)
                .subscribe({
                    callback.onResult(it, null)
                },{
                    Log.w("API", "failed to get after on list ${it.message}")
                    errors.value = IOException()
                    callback.onResult(emptyList(), null)
                }))
        }

        override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Item>) {}
    }

    override val inventoryList = LivePagedListBuilder<String, Item>(
        object : DataSource.Factory<String, Item>() {
            override fun create(): DataSource<String, Item> = InventoryListDataSource()
                .apply { dataSrc = this }
        }, 20)
        .setInitialLoadKey(null)
        .build()

    override fun add(name: String, quantity: Int): Single<out Item> {
        return Observable.just(Unit)
            .compose(apiCallConfig.apply(listClient.listPost(ItemPrototype(name, quantity))))
            .doOnNext { dataSrc.invalidate() }
            .singleOrError()
    }

    override fun remove(id: String): Single<out Item> {
        return Observable.just(Unit)
            .compose(apiCallConfig.apply(listClient.listDelete(id)))
            .doOnNext { dataSrc.invalidate() }
            .singleOrError()
    }

    override fun find(id: String): Single<out Item> {

        TODO()
    }

    private fun get(from: String?, limit: Int?) = Observable.just(Unit)
        .compose(apiCallConfig.apply(listClient.listGet(from, limit)))

    override fun changeName(id: String, newName: String): Single<out Item> {
        return Observable.just(Unit)
            .compose(
                apiCallConfig.apply(listClient.listPut(PartialItem(id, newName, null)))
            )
            .doOnNext { dataSrc.invalidate() }
            .singleOrError()
    }

    override fun changeQuantity(id: String, newQuantity: Int): Single<out Item> {
        return Observable.just(Unit)
            .compose(
                apiCallConfig.apply(
                    listClient.listPut(PartialItem(id, null, newQuantity))
                )
            )
            .doOnNext { dataSrc.invalidate() }
            .singleOrError()

    }
}
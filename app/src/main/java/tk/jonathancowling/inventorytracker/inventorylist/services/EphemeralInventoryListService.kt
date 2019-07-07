package tk.jonathancowling.inventorytracker.inventorylist.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import tk.jonathancowling.inventorytracker.clients.list.models.Item
import tk.jonathancowling.inventorytracker.util.Resettable
import tk.jonathancowling.inventorytracker.util.rx.AutoCompositeDisposable
import java.io.IOException
import java.util.*

internal class EphemeralInventoryListService : InventoryListService {

    override val errors = MutableLiveData<Throwable>()

    private val _inventoryList = mutableListOf<AndroidListItem>()

    private val dataSrc: InventoryListDataSource by Resettable({
        InventoryListDataSource()
    }, {
        it.isInvalid
    })

    inner class InventoryListDataSource : ItemKeyedDataSource<String, AndroidListItem>() {

        private val disposables: CompositeDisposable by AutoCompositeDisposable()

        override fun getKey(item: AndroidListItem) = item.id

        override fun loadInitial(
            params: LoadInitialParams<String>,
            callback: LoadInitialCallback<AndroidListItem>
        ) {
            disposables.add(Observable.just(Unit).subscribe({
                callback.onResult(_inventoryList.take(params.requestedLoadSize))
            }, {
                errors.value = IOException()
                callback.onResult(emptyList())
            }))
        }

        override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<AndroidListItem>) {
            params.requestedLoadSize
            disposables.add(Observable.just(Unit).subscribe({
                val from = _inventoryList.withIndex().single { it.value.id == params.key }.index + 1
                val to = if (from + params.requestedLoadSize < _inventoryList.size) {
                    from + params.requestedLoadSize
                } else {
                    _inventoryList.size
                }
                callback.onResult(_inventoryList.subList(from, to))
            }, {
                errors.value = IOException()
                callback.onResult(emptyList())
            }))
        }

        override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<AndroidListItem>) {}
    }

    @Suppress("UNCHECKED_CAST")
    override val inventoryList: LiveData<out PagedList<Item>> = LivePagedListBuilder(object : DataSource.Factory<String, AndroidListItem>(){
        override fun create(): DataSource<String, AndroidListItem> = dataSrc
    }, 20).build() as LiveData<out PagedList<Item>>

    override fun add(name: String, quantity: Int) = _inventoryList
        .indexOfFirst { !it.isInUse }
        .let { i ->
            return@let Single.just(i)
                .flatMap { index ->
                    if (index != -1) {
                        Single.just(_inventoryList.removeAt(i).id)
                    } else {
                        Single.just(UUID.randomUUID().toString())
                            .flatMap { uuid ->
                                find(uuid)
                                    .map { Pair(true, uuid) }
                                    .onErrorReturn { Pair(false, uuid) }
                            }.map {
                                if (it.first) throw RuntimeException() else it.second
                            }.retry(3)
                    }
                }.map {
                    AndroidListItem(it, name, quantity)
                }.map {
                    _inventoryList.add(0, it)
                    it
                }.doOnSuccess { dataSrc.invalidate() }
        }


    override fun remove(id: String) = Single.just(_inventoryList
        .single { it.id == id && it.isInUse }
        .also {
            it.isInUse = false
        }).doOnSuccess { dataSrc.invalidate() }

    override fun find(id: String) = Single.just(_inventoryList)
        .map { it.single { item -> item.id == id } }

    override fun changeName(id: String, newName: String) = Single.just(_inventoryList
        .mapIndexed { i, item -> Pair(i, item) }
        .single { it.second.id == id && it.second.isInUse }
        .let {
            _inventoryList[it.first] = AndroidListItem(
                it.second.id,
                newName,
                it.second.quantity
            )
            it.second
        }).doOnSuccess { dataSrc.invalidate() }

    override fun changeQuantity(id: String, newQuantity: Int) = Single.just(_inventoryList
        .mapIndexed { i, item -> Pair(i, item) }
        .single { it.second.id == id && it.second.isInUse }
        .let {
            _inventoryList[it.first] = AndroidListItem(
                it.second.id,
                it.second.name,
                newQuantity
            )
            it.second
        }).doOnSuccess { dataSrc.invalidate() }

    class AndroidListItem(item: Item, var isInUse: Boolean = true) : Item(item.id, item.name, item.quantity) {
        constructor(id: String, name: String, quantity: Int, isInUse: Boolean = true)
                : this(Item(id, name, quantity), isInUse)
    }
}
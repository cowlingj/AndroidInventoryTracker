package tk.jonathancowling.inventorytracker.inventorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.ObservableSource
import io.reactivex.Single
import tk.jonathancowling.inventorytracker.listclient.Item
import tk.jonathancowling.inventorytracker.util.prepend
import java.util.*

class LocalInventoryListService : InventoryListService {

    private val data: MutableLiveData<List<Item>> = MutableLiveData()
    private val shadowList = mutableListOf<AndroidListItem>()

    override val inventoryList: LiveData<out List<Item>> = data

    init {
        data.value = emptyList()
    }

    override fun add(name: String, quantity: Int) = shadowList
        .indexOfFirst { !it.isInUse }
        .let { i ->
            return@let Single.just(i)
                .flatMap { index ->
                    if (index != -1) {
                        Single.just(shadowList.removeAt(i).id)
                    } else {
                        Single.just(UUID.randomUUID().toString()).flatMap { uuid ->
                            find(uuid)
                                .map { Pair(true, uuid) }
                                .onErrorReturn { Pair(false, uuid) }
                        }.map { if (it.first) throw RuntimeException() else it.second }
                            .retry(3)
                    }
                }
                .map {
                    AndroidListItem(it, name, quantity)
                }.map {
                    shadowList.prepend(it)
                    sync(data.value.orEmpty().size + 1)
                    it
                }
        }


    override fun remove(id: String) = Single.just(shadowList
        .single { it.id == id && it.isInUse }
        .also {
            it.isInUse = false
            sync()
        })

    override fun find(id: String) = Single.just(data.value.orEmpty())
        .map { it.single { item -> item.id == id } }

    override fun pager(pageSize: Int) = ObservableSource<Unit> { ob ->
        if (data.value.orEmpty().size != shadowList.size) {
            sync(pageSize + data.value.orEmpty().size)
        }
        ob.onComplete()
    }

    override fun all() = Single.just(sync(shadowList.size))


    override fun changeName(id: String, newName: String) = Single.just(shadowList
        .mapIndexed { i, item -> Pair(i, item) }
        .single { it.second.id == id && it.second.isInUse }
        .let {
            shadowList[it.first] = AndroidListItem(it.second.id, newName, it.second.quantity)
            it.second
        }.also {
            sync()
        })

    override fun changeQuantity(id: String, newQuantity: Int) = Single.just(shadowList
        .mapIndexed { i, item -> Pair(i, item) }
        .single { it.second.id == id && it.second.isInUse }
        .let {
            shadowList[it.first] = AndroidListItem(it.second.id, it.second.name, newQuantity)
            it.second
        }.also {
            sync()
        })

    private fun sync(numberToSync: Int = data.value.orEmpty().size) {
        data.value = shadowList.filter { it.isInUse }.take(numberToSync)
    }
}
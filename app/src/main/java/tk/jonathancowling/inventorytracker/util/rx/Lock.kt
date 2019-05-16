package tk.jonathancowling.inventorytracker.util.rx

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class Lock {

    private var locked = false

    fun <UpT, DownT>useWith(criticalSection: Observable<DownT>) = ObservableTransformer<UpT, DownT> {
        it
            .doOnNext { if (locked) throw LockException() }
            .doOnNext { locked = true }
            .flatMap { criticalSection }
            .doOnEach { locked = false }
    }

    enum class LockUpdate { LockEvent, UnlockEvent }
    class LockException : RuntimeException()
}
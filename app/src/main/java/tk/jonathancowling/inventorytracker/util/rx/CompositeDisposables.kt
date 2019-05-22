package tk.jonathancowling.inventorytracker.util.rx

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import tk.jonathancowling.inventorytracker.util.Resettable

operator fun CompositeDisposable.plus(disposable: Disposable) {
    this.add(disposable)
}

class AutoCompositeDisposable: Resettable<CompositeDisposable>({ CompositeDisposable() }, { it.isDisposed })
package tk.jonathancowling.inventorytracker.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class AutoDisposable<DisposableT: Disposable>(private val create: () -> DisposableT) : ReadWriteProperty<Any?, DisposableT> {

    private var _value: DisposableT = create()

    override fun getValue(thisRef: Any?, property: KProperty<*>): DisposableT {
        if (_value.isDisposed) {
            _value = create()
        }
        return _value
    }


    override fun setValue(thisRef: Any?, property: KProperty<*>, value: DisposableT) {
        _value = value
    }

    class Composite: AutoDisposable<CompositeDisposable>({CompositeDisposable()})
}
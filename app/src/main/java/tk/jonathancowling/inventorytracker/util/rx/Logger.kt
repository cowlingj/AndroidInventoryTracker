package tk.jonathancowling.inventorytracker.util.rx

import android.util.Log
import io.reactivex.ObservableOperator
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class Logger<EventT>(private val priority: Int = Log.DEBUG,
                     tag: String? = null,
                     private val prefix: String = "") : ObservableOperator<EventT, EventT> {

    private val tag by lazy { tag ?: this::class.java.canonicalName }

    private fun log(msg: String) {
        Log.println(priority, tag, prefix + msg)
    }

    override fun apply(observer: Observer<in EventT>) = object : Observer<EventT> {
        override fun onComplete() {
            log("onComplete")
            observer.onComplete()
        }

        override fun onSubscribe(d: Disposable) {
            log("onSubscribe")
            observer.onSubscribe(d)
        }

        override fun onNext(t: EventT) {
            log("onNext - $t")
            observer.onNext(t)
        }

        override fun onError(e: Throwable) {
            log("onError - $e")
            observer.onError(e)
        }
    }
}
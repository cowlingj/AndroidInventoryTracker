package tk.jonathancowling.inventorytracker.util.rx

import io.reactivex.ObservableOperator
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.threeten.bp.Instant
import org.threeten.bp.temporal.TemporalAmount
import java.lang.RuntimeException

class Throttle<EventT>(private val timeout: TemporalAmount) :
    ObservableOperator<EventT, EventT> {
    override fun apply(observer: Observer<in EventT>) = object : Observer<EventT> {
        private lateinit var next: Instant

        override fun onComplete() { observer.onComplete() }

        override fun onSubscribe(d: Disposable) {
            next = Instant.now()
            observer.onSubscribe(d)
        }

        override fun onNext(t: EventT) {
            if (Instant.now() < next) {
                observer.onError(ThrottledException())
            } else {
                next = Instant.now() + timeout
                observer.onNext(t)
            }
        }

        override fun onError(e: Throwable) { observer.onError(e) }
    }

    class ThrottledException : RuntimeException()
}
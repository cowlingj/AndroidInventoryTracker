package tk.jonathancowling.inventorytracker.util.rx

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

open class Backoff<EventT>(
    private val maxRetries: Long,
    private val strategy: (Long) -> Observable<Long>
): ObservableTransformer<EventT, EventT> {
    override fun apply(upstream: Observable<EventT>): ObservableSource<EventT> {
        return upstream.retryWhen {
            it.zipWith<Long, Long>(
                Observable.rangeLong(0, maxRetries + 1),
                BiFunction { e: Throwable, retries: Long ->
                    if (retries >= maxRetries) {
                        throw e
                    }
                    retries
                }
            ).flatMap(strategy)
        }
    }

    class Constant<E>(maxRetries: Long, unit: TimeUnit = TimeUnit.SECONDS):
        Backoff<E>(maxRetries, { Observable.timer(it, unit) })

    class Linear<E>(maxRetries: Long, scale: Long = 1, unit: TimeUnit = TimeUnit.SECONDS):
        Backoff<E>(maxRetries, { Observable.timer(it * scale, unit) })

    class Exponential<E>(maxRetries: Long, base: Double = 2.0, unit: TimeUnit = TimeUnit.SECONDS):
        Backoff<E>(maxRetries, { Observable.timer(Math.pow(base, it.toDouble()).toLong(), unit) })
}
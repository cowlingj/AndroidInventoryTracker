package tk.jonathancowling.inventorytracker.util.rx

import io.reactivex.Observable
import org.reactivestreams.Publisher

fun <T> publisherFromObservable(ob: Observable<T>) = Publisher<T> {
    ob.subscribe(
        { t -> it.onNext(t) },
        { e -> it.onError(e) },
        { it.onComplete() }
    )
}

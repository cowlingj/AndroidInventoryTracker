package tk.jonathancowling.inventorytracker.util.rx

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.Duration

class ApiCallConfig {

    private val lock = Lock()

    private val throttler = Throttle<Unit>(Duration.ofSeconds(3))

    fun <DownT>apply(call :Observable<DownT>)
            = ObservableTransformer<Unit, DownT> {
        it
            .lift(throttler)
            .compose(lock.useWith(call))
            .compose(Backoff.Exponential(3))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}
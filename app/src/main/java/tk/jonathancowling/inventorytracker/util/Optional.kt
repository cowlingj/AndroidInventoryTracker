package tk.jonathancowling.inventorytracker.util

sealed class Optional<T> {

    abstract fun getNullable(): T?
    fun hasData() = getNullable()?.let { true } ?: false
    fun get() = getNullable()!!
    fun getOrElse(defaultValue: T) = if (hasData()) {
        get()
    } else {
        defaultValue
    }

    fun <R> map(notEmpty: (data: T) -> R?, empty: () -> R? = { null }) = transform {
        if (hasData()) {
            Optional.ofNullable(notEmpty(get()))
        } else {
            Optional.ofNullable(empty())
        }
    }

    fun <R> flatMap(notEmpty: (from: T) -> Optional<R>, empty: () -> Optional<R> = { Optional.empty() }) = transform {
        if (hasData()) {
            notEmpty(get())
        } else {
            empty()
        }
    }

    fun <R> transform(thenDo: (from: Optional<T>) -> Optional<R>) = thenDo(this)

    companion object {
        fun <T> of(t: T) = Optional.Data(t)
        fun <T> ofNullable(t: T?) = t?.let { Optional.Data(it) } ?: let { Optional.Empty<T>() }
        fun <T> empty() = Optional.Empty<T>()
    }

    class Data<T>(val data: T) : Optional<T>() {
        override fun getNullable() = data
    }

    class Empty<T> : Optional<T>() {
        override fun getNullable(): T? = null
    }
}
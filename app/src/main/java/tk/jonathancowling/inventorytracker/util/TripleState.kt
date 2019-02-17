package tk.jonathancowling.inventorytracker.util

import java.util.*

class TripleState<DataT> private constructor(private val data: DataT?, private val err: Throwable?, private val state: State) {

    fun <NewDataT> transform(transformer: (current: TripleState<DataT>)->TripleState<NewDataT>) = transformer(this)
    fun <NewDataT> transform(transformer: (data: DataT?, err: Throwable?, state: State)->TripleState<NewDataT>) = transformer(this.data, this.err, this.state)

    fun <NewDataT> transformWithMatchingState(
        data: (data: DataT)->TripleState<NewDataT>,
        err: (err: Throwable?)->TripleState<NewDataT> = { TripleState.fromError(it) },
        empty: ()->TripleState<NewDataT> = { TripleState.fromEmpty() }
    ) = when (state) {
        State.DATA -> data(this.data!!)
        State.ERROR -> err(this.err)
        State.EMPTY -> empty()
    }


    fun tap(f: (current: TripleState<DataT>)->Unit) = apply { f(this) }
    fun tap(f: (data: DataT?, err: Throwable?, state: State)->Unit) = apply { f(this.data, this.err, this.state) }
    fun tapWithMatchingState(
        data: (data: DataT)->Unit,
        err: (err: Throwable?)->Unit = {},
        empty: ()->Unit = {}
    ) = apply { when (state) {
        State.DATA -> data(this.data!!)
        State.ERROR -> err(this.err)
        State.EMPTY -> empty()
    } }

    fun <NewDataT> dataToData(f: (data: DataT)->NewDataT) = transformWithMatchingState({ fromData(f(it)) })
    fun <NewDataT> dataToDataOrEmpty(f: (data: DataT)->NewDataT?) = transformWithMatchingState({ fromDataOrEmpty(f(it)) })
    fun <NewDataT> dataToDataEmptyOrError(f: (data: DataT)->NewDataT?) = transformWithMatchingState({ tryDataOrEmpty { f(it) } })

    fun isEmptyState() = this.state == State.EMPTY
    fun isDataState() = this.state == State.DATA
    fun isErrorState() = this.state == State.ERROR

    override fun hashCode(): Int {
        return Objects.hash(data, state)
    }

    override fun equals(other: Any?): Boolean {
        return this.hashCode() == other.hashCode() || super.equals(other)
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName} { (data: ${data ?: "null"}, err: ${err ?: "null"}, state: $state }"
    }

    companion object {
        fun <DataT> fromData(data: DataT): TripleState<DataT> = TripleState(data, null, State.DATA)
        fun <DataT> fromError(err: Throwable? = null): TripleState<DataT> = TripleState(null, err, State.ERROR)
        fun <DataT> fromEmpty(): TripleState<DataT> = TripleState(null, null, State.EMPTY)

        fun <DataT> fromDataOrEmpty(data: DataT?): TripleState<DataT> = data?.let { fromData(it) } ?: fromEmpty()
        fun <DataT> tryDataOrEmpty(source: () -> DataT?) = try {
            fromDataOrEmpty(source())
        } catch (err: Throwable) {
            fromError<DataT>(err)
        }
    }

    enum class State {
        DATA, ERROR, EMPTY
    }
}
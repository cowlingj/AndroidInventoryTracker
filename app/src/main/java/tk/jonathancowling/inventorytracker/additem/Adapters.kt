package tk.jonathancowling.inventorytracker.additem

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.*
import tk.jonathancowling.inventorytracker.util.TripleState

object Adapters {
    @JvmStatic
    @BindingAdapter("add_item_quantity")
    fun setText(editText: EditText, prev: TripleState<Int>) {
        val cur: TripleState<Int> = getText(editText)

        if (cur == prev) {
            return
        }

        cur.tapWithMatchingState({
            editText.setText(it.toString())
        }, {
            prev.tapWithMatchingState({ editText.setText(it.toString()) })
        }, {
            editText.setText("")
        })
    }

    @JvmStatic
    @BindingAdapter("add_item_quantityAttrChanged")
    fun onChange(editText: EditText, listener: InverseBindingListener) {
        editText.addTextChangedListener(object : TextWatcher {
            private var prev: CharSequence? = null

            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                prev = p0
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 == prev) {
                    return
                }
                listener.onChange()
            }
        })
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "add_item_quantity")
    fun getText(editText: EditText?): TripleState<Int> = editText?.text.let {
        TripleState.tryDataOrEmpty { if (it?.isEmpty() != false) null else Integer.parseInt(it.toString()) }
    }
}
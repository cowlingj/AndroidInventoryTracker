package tk.jonathancowling.inventorytracker.additem

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.databinding.*

object Adapters {
    @JvmStatic
    @BindingAdapter("add_item_quantity")
    fun setText(editText: EditText, int: Int?) {
        val stringRepr = int?.toString() ?: ""
        if (editText.text.toString() == stringRepr) {
            return
        }
        editText.setText(stringRepr)
    }

    @JvmStatic
    @BindingAdapter("add_item_quantityAttrChanged")
    fun onChange(editText: EditText, listener: InverseBindingListener) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i("ADAPTER", "listener onChange fired")
                listener.onChange()
            }
        })
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "add_item_quantity")
    fun getText(editText: EditText?): Int?
            = editText?.text.let { if (it != null && it.isNotEmpty()) Integer.parseInt(it.toString()).apply {
        Log.i("INVADAPTER", "int is $this")
    } else null }
}
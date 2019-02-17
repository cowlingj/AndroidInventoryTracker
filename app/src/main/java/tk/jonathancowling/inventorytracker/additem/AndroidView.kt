package tk.jonathancowling.inventorytracker.additem


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_add_item.*

import tk.jonathancowling.inventorytracker.databinding.FragmentAddItemBinding
import tk.jonathancowling.inventorytracker.inventorylist.InventoryListObservable

class AndroidView : Fragment() {

    private lateinit var vm: AddItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val observable = AddItemObservable()

        vm = ViewModelProviders.of(this, AddItemViewModel.Factory(observable)).get(AddItemViewModel::class.java)

        val binding = FragmentAddItemBinding.inflate(inflater, container, false)
        binding.addItem = observable
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.data.observe(this, Observer {
            Log.i("BUTTON", "enabled: ${vm.isValid()}")
            add_item_add_button.isEnabled = vm.isValid()
        })

        add_item_add_button.setOnClickListener {

            (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(activity?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            vm.validate({ name, quantity ->
                ViewModelProviders.of(activity!!).get(InventoryListObservable::class.java)
                    .addItem(name, quantity)
                findNavController().navigateUp()
            })
        }
    }
}

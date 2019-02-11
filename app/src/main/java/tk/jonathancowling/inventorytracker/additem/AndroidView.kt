package tk.jonathancowling.inventorytracker.additem


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_add_item.*

import tk.jonathancowling.inventorytracker.databinding.FragmentAddItemBinding
import tk.jonathancowling.inventorytracker.inventorylist.InventoryListObservable

class AndroidView : Fragment() {

    private lateinit var binding: FragmentAddItemBinding
    private lateinit var vm: AddItemViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddItemBinding.inflate(inflater, container, false)
        binding.addItem = AddItemObservable()
        binding.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                vm.setData(sender as AddItemObservable)
                Log.i("VIEW", "vm updated")
            }
        })
        binding.setLifecycleOwner(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProviders.of(this).get(AddItemViewModel::class.java)

        vm.data.observe(this, Observer {
            add_item_add_button.isEnabled = vm.isValid()
        })

        add_item_add_button.setOnClickListener {
            vm.data.value?.let {
                Log.i("VIEW", "view model has value { ${it.name}, ${it.quantity} }")
                ViewModelProviders.of(activity!!).get(InventoryListObservable::class.java)
                    .addItem(it.name, it.quantity)
                findNavController().popBackStack()
            }
            Log.i("VIEW", "button clicked")
        }
    }
}

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
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.add_item_fragment.*

import tk.jonathancowling.inventorytracker.databinding.AddItemFragmentBinding
import tk.jonathancowling.inventorytracker.inventorylist.ApiInventoryListService
import tk.jonathancowling.inventorytracker.inventorylist.InventoryListViewModel
import tk.jonathancowling.inventorytracker.util.AutoDisposable

class AndroidView : Fragment() {

    private lateinit var vm: AddItemViewModel
    private val disposable: CompositeDisposable by AutoDisposable.Composite()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val observable = AddItemObservable()

        vm = ViewModelProviders.of(this, AddItemViewModel.Factory(observable)).get(AddItemViewModel::class.java)

        val binding = AddItemFragmentBinding.inflate(inflater, container, false)
        binding.addItem = observable
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.data.observe(this, Observer { add_item_add_button.isEnabled = vm.isValid() })

        add_item_add_button.setOnClickListener {

            (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(activity?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            vm.validate({ name, quantity ->
                ViewModelProviders.of(
                    activity!!,
                    InventoryListViewModel.Factory(ApiInventoryListService())
                ).get(InventoryListViewModel::class.java).let {
                    var hasBeenCalled = false
                    it.getErrors().observe(this, Observer {
                        Snackbar.make(view, "add item failed", Snackbar.LENGTH_SHORT).show()
                    })

                    it.getData().observe(this, Observer { items ->
                        Log.d("ADD_ITEM_VIEW", "observe triggered ${items.size}")
                        if (hasBeenCalled) {
                            findNavController().navigateUp()
                        }
                        hasBeenCalled = true
                    })
                    disposable.add(it.addItem(name, quantity).subscribe({ item ->
                        Log.d("VIEW", "item added $item")
                    }, { e ->
                        Log.e("VIEW", "error adding item $e")
                    }))
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}

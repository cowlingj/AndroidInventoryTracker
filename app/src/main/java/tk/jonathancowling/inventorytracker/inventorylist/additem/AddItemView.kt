package tk.jonathancowling.inventorytracker.inventorylist.additem


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.add_item_fragment.*
import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.authentication.FirebaseAuthViewModel
import tk.jonathancowling.inventorytracker.databinding.AddItemFragmentBinding
import tk.jonathancowling.inventorytracker.inventorylist.InventoryListViewModel
import tk.jonathancowling.inventorytracker.util.existingKeyedScope
import tk.jonathancowling.inventorytracker.util.rx.AutoCompositeDisposable

class AddItemView : Fragment() {

    private val userScope by existingKeyedScope()
    private val listScope by existingKeyedScope()

    private val disposable: CompositeDisposable by AutoCompositeDisposable()
    private val addItemObservable: AddItemObservable = AddItemObservable()

    private val addItemViewModel: AddItemViewModel by viewModels(
        factoryProducer = { AddItemViewModel.Factory(addItemObservable) }
    )

    private val authViewModel: FirebaseAuthViewModel by viewModels(
        ownerProducer =  { ViewModelStoreOwner { userScope.store } }
    )

    private val inventoryListViewModel : InventoryListViewModel by viewModels(
        ownerProducer =  { ViewModelStoreOwner { listScope.store } }
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        authViewModel.user.observe(this, Observer {
            if (it is FirebaseAuthViewModel.AuthState.LoggedOut) {
                findNavController().navigate(R.id.login_destination)
            }
        })

        val observable = addItemObservable

        val binding = AddItemFragmentBinding.inflate(inflater, container, false)
        binding.addItem = observable
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var model: AddItemViewModel.ModelWithValidation.ValidModel? = null

        addItemViewModel.data.observe(this, Observer {
            when (it) {
                is AddItemViewModel.ModelWithValidation.ValidModel -> {
                    add_item_add_button.isEnabled = true
                    model = it
                }
                is AddItemViewModel.ModelWithValidation.InvalidModel -> {
                    add_item_add_button.isEnabled = false
                    model = null
                }
            }
        })


        authViewModel.user.observe(this, Observer {
            when (it) {
                is FirebaseAuthViewModel.AuthState.LoggedOut -> {
                    findNavController().navigate(R.id.login_destination)
                }
                is FirebaseAuthViewModel.AuthState.LoggedIn -> {
                    setAddButton { model }
                }
            }
        })
    }

    private fun setAddButton(
        modelProvider: () -> AddItemViewModel.ModelWithValidation.ValidModel?
    ) {

        add_item_add_button.setOnClickListener {

            (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(activity?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            val model = modelProvider()
            if (model == null) {
                view?.let {
                    Snackbar
                        .make(it, "oops, something went wrong", Snackbar.LENGTH_LONG)
                        .show()
                }
                Log.e(tag, "no model found")
                return@setOnClickListener
            }

            try {
                inventoryListViewModel.addItem(model.name, model.quantity).subscribe({ item ->
                    Log.d(tag, "item added $item")
                    findNavController().navigateUp()
                }, { e ->
                        Snackbar
                            .make(requireView(), "oops, something went wrong", Snackbar.LENGTH_LONG)
                            .show()

                    Log.e(tag, "error adding item $e")
                }).apply {
                    disposable.add(this)
                }
            } catch (npe: NullPointerException) {
                Log.w(tag, "InventoryListServiceProvider threw an exception: $npe")
                Snackbar.make(
                    requireView(),
                    "cloud sync has not configured properly, please check settings",
                    Snackbar.LENGTH_SHORT
                ).show()
           }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}

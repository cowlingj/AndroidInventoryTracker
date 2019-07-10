package tk.jonathancowling.inventorytracker.authentication.containers

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.ScopeViewModel
import tk.jonathancowling.inventorytracker.authentication.FirebaseAuthViewModel
import tk.jonathancowling.inventorytracker.inventorylist.InventoryListViewModel
import tk.jonathancowling.inventorytracker.settings.SettingsViewModel
import tk.jonathancowling.inventorytracker.util.existingScope
import tk.jonathancowling.inventorytracker.util.lazyReadOnlyProperty
import tk.jonathancowling.inventorytracker.util.newScopeKey

class LoggedInContainer : Fragment() {

    private val userScope by existingScope()
    private val listScope by lazyReadOnlyProperty { newScopeKey(userScope.key) }

    private val settingsVM by viewModels<SettingsViewModel>(
        ownerProducer = { ViewModelStoreOwner { userScope.store } }
    )

    private val authVM by viewModels<FirebaseAuthViewModel>(
        ownerProducer = { ViewModelStoreOwner { userScope.store } }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.container, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewModelProvider({
            ViewModelProviders.of(requireActivity())[ScopeViewModel::class.java][listScope]!!
        }, InventoryListViewModel.Factory(settingsVM.repository)).get<InventoryListViewModel>()

        NavHostFragment.create(
            R.navigation.inventory_list_navigation,
            Bundle().apply {
                putInt("userScope", userScope.key)
                putInt("listScope", listScope)
            }
        ).let {
            fragmentManager?.commit {
                replace(R.id.container, it)
                setPrimaryNavigationFragment(it)
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_logout -> {
                authVM.logout()
                true
            }
            R.id.menu_settings -> {
                fragmentManager
                    ?.findFragmentById(R.id.container)
                    ?.findNavController()
                    ?.navigate(R.id.settings_destination, Bundle().apply {
                        putInt("userScope", userScope.key)
                    })
                true
            }
            R.id.menu_report -> {
                fragmentManager
                    ?.findFragmentById(R.id.container)
                    ?.findNavController()
                    ?.navigate(R.id.report_destination, Bundle().apply {
                        putInt("userScope", userScope.key)
                        putInt("listScope", listScope)
                    })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

}
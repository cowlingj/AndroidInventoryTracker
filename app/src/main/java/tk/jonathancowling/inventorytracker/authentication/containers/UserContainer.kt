package tk.jonathancowling.inventorytracker.authentication.containers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import tk.jonathancowling.inventorytracker.BuildConfig
import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.ScopeViewModel
import tk.jonathancowling.inventorytracker.authentication.FirebaseAuthViewModel
import tk.jonathancowling.inventorytracker.settings.SettingsViewModel
import tk.jonathancowling.inventorytracker.util.newScope

class UserContainer : Fragment() {

    private val scope by newScope()

    private val scopeVM: ScopeViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.user_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences: Subject<SettingsViewModel.NamedSharedPreferences> = BehaviorSubject.create()

        ViewModelProvider(scope.store, SettingsViewModel.Factory(sharedPreferences))
            .get(SettingsViewModel::class.java)

        ViewModelProvider(scope.store,
            FirebaseAuthViewModel.Factory()
        )
            .get<FirebaseAuthViewModel>()
            .user
            .observe(this, Observer {

                scopeVM.popChildren(scope.key)

                when (it) {
                    is FirebaseAuthViewModel.AuthState.LoggedIn -> {
                        "${it.user.uid}.${BuildConfig.APPLICATION_ID}".let { name ->
                            sharedPreferences.onNext(
                                SettingsViewModel.NamedSharedPreferences(
                                    name,
                                    requireContext().getSharedPreferences(name, Context.MODE_PRIVATE)
                                )
                            )
                        }

                        childFragmentManager
                            .primaryNavigationFragment!!
                            .findNavController()
                            .navigate(
                                R.id.loggedInContainer,
                                Bundle().apply {
                                    putInt("userScope", scope.key)
                                }
                            )

                    } else -> {
                    BuildConfig.APPLICATION_ID.let { name ->
                        sharedPreferences.onNext(
                            SettingsViewModel.NamedSharedPreferences(
                                name,
                                requireContext().getSharedPreferences(name, Context.MODE_PRIVATE)
                            )
                        )
                    }


                        childFragmentManager
                            .primaryNavigationFragment!!
                            .findNavController()
                            .navigate(
                                R.id.loggedOutContainer,
                                Bundle().apply {
                                    this.putInt("userScope", scope.key)
                                }
                            )
                    }
                }
            })
    }
}
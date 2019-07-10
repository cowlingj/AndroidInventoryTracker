package tk.jonathancowling.inventorytracker.authentication.containers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.util.existingScopeKey

class LoginContainer : Fragment() {

    private val userScope by existingScopeKey()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.container, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NavHostFragment.create(
            R.navigation.login,
            Bundle().apply {
                putInt("userScope", userScope)
            }
        ).let {
            fragmentManager?.commit {
                replace(R.id.container, it)
                setPrimaryNavigationFragment(it)
            }
        }
    }
}
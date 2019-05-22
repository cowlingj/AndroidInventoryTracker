package tk.jonathancowling.inventorytracker.authentication


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.auth_login_content.view.*
import kotlinx.android.synthetic.main.util_logo_with_card_fragment.view.*
import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.databinding.AuthLoginContentBinding

class AndroidLoginView : Fragment() {

    private val observable = EmailPasswordObservable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.util_logo_with_card_fragment, container, false)
        .apply {
            val binding = AuthLoginContentBinding.inflate(inflater, logo_with_card_content, true)
            binding.emailPassword = observable
            binding.lifecycleOwner = this@AndroidLoginView
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm = ViewModelProviders.of(requireActivity(), FirebaseAuthViewModel.Factory()).get<FirebaseAuthViewModel>()

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Snackbar.make(view, "please login to continue", Snackbar.LENGTH_SHORT).show()
            }
        })

        vm.user.observe(this, Observer {
            if (it is FirebaseAuthViewModel.AuthState.LoggedIn) {
                findNavController().popBackStack(R.id.inventory_list_destination, false)
            }
        })

        vm.error.observe(this, Observer {
            Snackbar.make(view, "login failed", Snackbar.LENGTH_SHORT).show()
            Log.w(this.tag, "failed login, exception: $it")
        })

        view.login_button_login.setOnClickListener {
            vm.login(observable.getEmail(), observable.getPassword())
        }

        view.login_button_signup.setOnClickListener {
            findNavController().navigate(R.id.sign_up_destination)
        }
    }
}

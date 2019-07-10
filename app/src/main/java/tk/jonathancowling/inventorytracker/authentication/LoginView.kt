package tk.jonathancowling.inventorytracker.authentication


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.auth_login_content.view.*
import kotlinx.android.synthetic.main.util_logo_with_card_fragment.view.*
import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.databinding.AuthLoginContentBinding
import tk.jonathancowling.inventorytracker.util.existingScope

class LoginView : Fragment() {

    private val userScope by existingScope()

    private val observable = EmailPasswordObservable()

    private val authViewModel: FirebaseAuthViewModel by viewModels(
        ownerProducer = { ViewModelStoreOwner { userScope.store } }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.util_logo_with_card_fragment, container, false)
        .apply {
            val binding = AuthLoginContentBinding.inflate(inflater, logo_with_card_content, true)
            binding.emailPassword = observable
            binding.lifecycleOwner = this@LoginView
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Snackbar.make(view, "please login to continue", Snackbar.LENGTH_SHORT).show()
            }
        })

        authViewModel.user.observe(this, Observer {
            if (it is FirebaseAuthViewModel.AuthState.Failed) {
                Snackbar.make(view, "login failed", Snackbar.LENGTH_SHORT).show()
                Log.w(this.tag, "failed login, exception: $it")
            }
        })

        view.login_button_login.setOnClickListener {
            authViewModel.login(observable.getEmail(), observable.getPassword())
        }

        view.login_button_signup.setOnClickListener {
            findNavController().navigate(R.id.signup_destination, Bundle().apply {
                putInt("userScope", userScope.key)
            })
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity()
            .window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onStop() {
        super.onStop()
        requireActivity()
            .window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
    }
}

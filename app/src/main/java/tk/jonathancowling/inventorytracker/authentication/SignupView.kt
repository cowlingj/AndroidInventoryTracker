package tk.jonathancowling.inventorytracker.authentication


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.auth_sign_up_content.view.*
import kotlinx.android.synthetic.main.util_logo_with_card_fragment.view.*
import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.databinding.AuthSignUpContentBinding
import tk.jonathancowling.inventorytracker.util.existingScope

class SignupView : Fragment() {

    private val observable = EmailPasswordObservable()

    private val userScope by existingScope()

    private val authViewModel: FirebaseAuthViewModel by viewModels(
        ownerProducer = { ViewModelStoreOwner { userScope.store } }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.util_logo_with_card_fragment, container, false)
        .apply {
            val binding = AuthSignUpContentBinding.inflate(inflater, this.logo_with_card_content, true)
            binding.emailPassword = observable
            binding.lifecycleOwner = this@SignupView
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.user.observe(this, Observer {
            if (it is FirebaseAuthViewModel.AuthState.Failed) {
                Snackbar.make(view, "sign up failed", Snackbar.LENGTH_SHORT).show()
                Log.w(this.tag, "failed sign up, exception: $it")
            }
        })

        view.sign_up_button_sign_up.setOnClickListener {
            authViewModel.signUp(observable.getEmail(), observable.getPassword())
        }

        view.sign_up_button_anonymous.setOnClickListener {
            authViewModel.anon()
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

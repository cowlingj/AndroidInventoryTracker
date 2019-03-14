package tk.jonathancowling.inventorytracker.authentication


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.auth_sign_up_content.view.*
import kotlinx.android.synthetic.main.util_logo_with_card_fragment.view.*
import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.authentication.services.FirebaseAuthMechanisms
import tk.jonathancowling.inventorytracker.authentication.services.FirebaseAuthService
import tk.jonathancowling.inventorytracker.databinding.AuthSignUpContentBinding

class AndroidSignUpView : Fragment() {

    lateinit var vm: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.util_logo_with_card_fragment, container, false)
        .apply {
            val binding = AuthSignUpContentBinding.inflate(inflater, this.logo_with_card_content, true)
            val observable = EmailPasswordObservable()
            binding.emailPassword = observable
            binding.lifecycleOwner = this@AndroidSignUpView

            vm = ViewModelProviders.of(this@AndroidSignUpView, AuthViewModel.Factory(observable)).get()
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.sign_up_button_sign_up.setOnClickListener {
            vm.validate().flatMap {
                FirebaseAuthService.Factory().create()
                    .signUp(FirebaseAuthMechanisms.signUpEmailPassword(it.email, it.password))
            }.subscribe({
                findNavController().popBackStack(R.id.inventory_list_destination, false)
            }, {
                Snackbar.make(view, "sign up failed", Snackbar.LENGTH_SHORT).show()
            })
        }

        view.sign_up_button_anonymous.setOnClickListener {
            FirebaseAuthService.Factory().create()
                .login(FirebaseAuthMechanisms.continueAnonymously())
                .subscribe({
                    findNavController().popBackStack(R.id.inventory_list_destination, false)
                }, {
                    Snackbar.make(view, "login failed", Snackbar.LENGTH_SHORT).show()
                })
        }
    }
}

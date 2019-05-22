package tk.jonathancowling.inventorytracker.authentication


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.auth_sign_up_content.view.*
import kotlinx.android.synthetic.main.util_logo_with_card_fragment.view.*
import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.databinding.AuthSignUpContentBinding

class AndroidSignUpView : Fragment() {

    private val observable = EmailPasswordObservable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.util_logo_with_card_fragment, container, false)
        .apply {
            val binding = AuthSignUpContentBinding.inflate(inflater, this.logo_with_card_content, true)
            binding.emailPassword = observable
            binding.lifecycleOwner = this@AndroidSignUpView
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm = ViewModelProviders.of(requireActivity(), FirebaseAuthViewModel.Factory()).get<FirebaseAuthViewModel>()

        vm.user.observe(this, Observer {
            if (it is FirebaseAuthViewModel.AuthState.LoggedIn) {
                findNavController().popBackStack(R.id.inventory_list_destination, false)
            }
        })

        vm.error.observe(this, Observer {
            Snackbar.make(view, "sign up failed", Snackbar.LENGTH_SHORT).show()
            Log.w(this.tag, "failed sign up, exception: $it")
        })

        view.sign_up_button_sign_up.setOnClickListener {
            vm.signUp(observable.getEmail(), observable.getPassword())
        }

        view.sign_up_button_anonymous.setOnClickListener {
            vm.anon()
        }
    }
}

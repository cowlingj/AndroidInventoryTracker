package tk.jonathancowling.inventorytracker.authentication


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.content_login.view.*
import kotlinx.android.synthetic.main.content_sign_up.view.*
import kotlinx.android.synthetic.main.fragment_logo_with_card.view.*

import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.databinding.ContentSignUpBinding

class AndroidSignUpView : Fragment() {

    lateinit var vm: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_logo_with_card, container, false)
        .apply {
            val binding = ContentSignUpBinding.inflate(inflater, this.logo_with_card_content, true)
            val observable = EmailPasswordObservable()
            binding.emailPassword = observable
            binding.lifecycleOwner = this@AndroidSignUpView

            vm = ViewModelProviders.of(this@AndroidSignUpView, AuthViewModel.Factory(observable)).get()
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.sign_up_button_sign_up.setOnClickListener {
            vm.validate({}, {
                FirebaseAuth.getInstance().apply {
                    this.addAuthStateListener(object : FirebaseAuth.AuthStateListener {
                        override fun onAuthStateChanged(auth: FirebaseAuth) {
                            auth.currentUser ?: return
                            auth.removeAuthStateListener(this)
                            findNavController().popBackStack(R.id.inventory_list_destination, false)
                        }
                    })
                    signInWithEmailAndPassword(it.email, it.password)
                        .addOnFailureListener {
                            Snackbar.make(view, "sign up failed", Snackbar.LENGTH_SHORT).show()
                        }
                }
            })
        }

        view.sign_up_button_anonymous.setOnClickListener {
            FirebaseAuth.getInstance().apply {
                this.addAuthStateListener(object : FirebaseAuth.AuthStateListener {
                    override fun onAuthStateChanged(auth: FirebaseAuth) {
                        auth.currentUser ?: return
                        auth.removeAuthStateListener(this)
                        findNavController().popBackStack(R.id.inventory_list_destination, false)
                    }
                })
                FirebaseAuth.getInstance().signInAnonymously()
            }
        }
    }
}

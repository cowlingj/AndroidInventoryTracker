package tk.jonathancowling.inventorytracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container)
        setSupportActionBar(toolbar)
        NavHostFragment.create(R.navigation.activity).let {
            supportFragmentManager.commit {
                replace(R.id.container, it)
                setPrimaryNavigationFragment(it)
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        super.onCreateOptionsMenu(menu)
//        return true
//    }

    override fun onSupportNavigateUp()
            = findNavController(R.layout.activity).navigateUp()
}

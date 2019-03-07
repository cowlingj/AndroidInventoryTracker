package tk.jonathancowling.inventorytracker

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

import kotlinx.android.synthetic.main.inventory_list_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inventory_list_activity)
        setSupportActionBar(toolbar)
    }

    override fun onSupportNavigateUp()
            = findNavController(R.layout.inventory_list_activity).navigateUp()
}

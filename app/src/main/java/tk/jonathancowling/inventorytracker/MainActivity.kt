package tk.jonathancowling.inventorytracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.inventory_list_activity.*
import tk.jonathancowling.inventorytracker.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inventory_list_activity)
        setSupportActionBar(toolbar)
    }
}

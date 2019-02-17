package tk.jonathancowling.inventorytracker.inventorylist


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_item_list.*

import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.databinding.InventoryListItemBinding

class AndroidView : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vm = ViewModelProviders.of(activity!!).get(InventoryListObservable::class.java)
        var data: List<ListItem> = emptyList()

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_inventory_list_to_add_item)
        }

        val adapter = object : RecyclerView.Adapter<ListItemHolder>() {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
                ListItemHolder(InventoryListItemBinding.inflate(layoutInflater,
                                                       p0,
                                          false))

            override fun getItemCount() = data.size

            override fun onBindViewHolder(vh: ListItemHolder, i: Int) {
                vh.binding.item = data[i]
            }
        }

        inventory_list.adapter = adapter
        inventory_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        vm.getData().observe(this, Observer {
            data = it
            adapter.notifyDataSetChanged()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    class ListItemHolder(val binding: InventoryListItemBinding) : RecyclerView.ViewHolder(binding.root)

}

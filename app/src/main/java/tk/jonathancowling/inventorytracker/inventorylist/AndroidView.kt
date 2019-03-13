package tk.jonathancowling.inventorytracker.inventorylist

import android.os.Bundle
import android.view.*
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_item_list.*
import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.authentication.services.FirebaseAuthService
import tk.jonathancowling.inventorytracker.communications.AndroidStringFetcher
import tk.jonathancowling.inventorytracker.communications.CommunicationsChannelManager
import tk.jonathancowling.inventorytracker.databinding.InventoryListItemBinding

class AndroidView : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.applicationContext?.let {
            CommunicationsChannelManager(
                NotificationManagerCompat.from(it),
                AndroidStringFetcher(it)::getString
            )
                .apply {
                    subscribe(
                        CommunicationsChannelManager.Channel.ESSENTIAL,
                        CommunicationsChannelManager.Channel.DEFAULT,
                        CommunicationsChannelManager.Channel.REPORT
                    )
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_item_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseAuthService.Factory().create()
            .withUser().map({}, {
                findNavController().navigate(R.id.login_destination)
            })

        val vm = ViewModelProviders.of(activity!!).get(InventoryListObservable::class.java)
        var data: List<ListItem> = emptyList()

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_inventory_list_to_add_item)
        }

        val adapter = object : RecyclerView.Adapter<ListItemHolder>() {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
                ListItemHolder(
                    InventoryListItemBinding.inflate(
                        layoutInflater,
                        p0,
                        false
                    )
                )

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
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        item.onNavDestinationSelected(findNavController()) || when (item.itemId) {
            R.id.menu_logout -> {
                FirebaseAuth.getInstance().signOut()
                activity?.recreate()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    class ListItemHolder(val binding: InventoryListItemBinding) : RecyclerView.ViewHolder(binding.root)
}

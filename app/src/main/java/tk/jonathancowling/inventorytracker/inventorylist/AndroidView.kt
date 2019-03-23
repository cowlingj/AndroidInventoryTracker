package tk.jonathancowling.inventorytracker.inventorylist

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
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
import kotlinx.android.synthetic.main.inventory_list_content.*
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
    ): View = inflater.inflate(R.layout.inventory_list_content, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseAuthService.Factory().create()
            .withUser().map({}, {
                findNavController().navigate(R.id.login_destination)
            })

        val vm = ViewModelProviders.of(
            activity!!,
            InventoryListViewModel.Factory(LocalInventoryListService())
        ).get(InventoryListViewModel::class.java)

        var data: List<ListItem> = emptyList()

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_inventory_list_to_add_item)
        }

        val adapter = object : RecyclerView.Adapter<ListItemHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                ListItemHolder(
                    InventoryListItemBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )

            override fun getItemCount() = data.size

            override fun onBindViewHolder(vh: ListItemHolder, i: Int) {
                vh.binding.item = data[i]
                if (isOnLastPage(i, vh.binding.root.height)) {
                    vm.pager()
                }
            }

            fun isOnLastPage(itemAt: Int, itemHeight: Int) =
                itemHeight == 0 || activity?.let {
                    itemCount - itemAt < (it.window.decorView.height / itemHeight)
                } ?: false
        }

        inventory_list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
                    outRect.bottom = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        resources.getDimension(R.dimen.inventory_list_bottom_padding),
                        resources.displayMetrics
                    ).toInt()
                }
            }
        })
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

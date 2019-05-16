package tk.jonathancowling.inventorytracker.inventorylist

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.paging.RxPagedListBuilder
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.inventory_list_content.*
import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.authentication.services.FirebaseAuthService
import tk.jonathancowling.inventorytracker.communications.AndroidStringFetcher
import tk.jonathancowling.inventorytracker.communications.CommunicationsChannelManager
import tk.jonathancowling.inventorytracker.databinding.InventoryListItemBinding
import tk.jonathancowling.inventorytracker.clients.list.models.Item
import tk.jonathancowling.inventorytracker.util.AutoDisposable

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
            InventoryListViewModel.Factory(ApiInventoryListService())
        ).get(InventoryListViewModel::class.java)

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_inventory_list_to_add_item)
        }

        val adapter : PagedListAdapter<Item, ListItemHolder> = object : PagedListAdapter<Item, ListItemHolder>(object : DiffUtil.ItemCallback<Item>(){
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem == newItem

        }){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
                return ListItemHolder(
                    InventoryListItemBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
            }

            override fun onBindViewHolder(vh: ListItemHolder, i: Int) {
                vh.binding.item = getItem(i)
            }
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

        vm.getErrors().observe(this, Observer {
            Snackbar.make(view, "get items failed", Snackbar.LENGTH_INDEFINITE).show()
        })

        vm.getData().observe(this, Observer {
            adapter.submitList(it)
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

package tk.jonathancowling.inventorytracker.inventorylist.listitems

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.inventory_list_content.*
import tk.jonathancowling.inventorytracker.R
import tk.jonathancowling.inventorytracker.clients.list.models.Item
import tk.jonathancowling.inventorytracker.communications.AndroidStringFetcher
import tk.jonathancowling.inventorytracker.communications.CommunicationsChannelManager
import tk.jonathancowling.inventorytracker.databinding.InventoryListItemBinding
import tk.jonathancowling.inventorytracker.inventorylist.InventoryListViewModel
import tk.jonathancowling.inventorytracker.util.existingScope

class HomeView : Fragment() {

    private val userScope by existingScope()
    private val listScope by existingScope()

    private val inventoryListViewModel: InventoryListViewModel by viewModels(
        ownerProducer = { ViewModelStoreOwner { listScope.store } }
    )

    private val adapter =
        object : PagedListAdapter<Item, ListItemHolder>(object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(
                oldItem: Item,
                newItem: Item
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Item,
                newItem: Item
            ): Boolean = oldItem.name == newItem.name && oldItem.quantity == newItem.quantity

    }) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
                return ListItemHolder(InventoryListItemBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                ))
            }

            override fun onBindViewHolder(vh: ListItemHolder, i: Int) {
                vh.binding.item = getItem(i)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.applicationContext?.let {
            CommunicationsChannelManager(
                NotificationManagerCompat.from(it),
                AndroidStringFetcher(it)::getString
            ).apply {
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

        setUpList()

        fab.setOnClickListener {
            findNavController().navigate(R.id.home_to_additem,
                Bundle().apply {
                    putInt("userScope", userScope.key)
                    putInt("listScope", listScope.key)
                })
        }

        inventory_list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                if (parent.getChildAdapterPosition(view) == parent.layoutManager!!.itemCount - 1) {
                    outRect.bottom = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        resources.getDimension(R.dimen.inventory_list_bottom_padding),
                        resources.displayMetrics
                    ).toInt()
                } else {
                    outRect.bottom = 0
                }
            }
        })

        inventory_list.adapter = adapter
        inventory_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun setUpList() {

        try {
            inventoryListViewModel.getErrors().observe(this, Observer { e ->
                Snackbar.make(requireView(), "get items failed", Snackbar.LENGTH_LONG).show()
                Log.w(this.tag, "get items failed, exception: $e")
            })

            inventoryListViewModel.getData().observe(this, Observer {
                adapter.submitList(it)
            })

        } catch (npe: NullPointerException) {
            Log.w(tag, "InventoryListServiceProvider threw an exception: $npe")
            Snackbar.make(
                requireView(),
                "cloud sync has not configured properly, please check settings",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    class ListItemHolder(val binding: InventoryListItemBinding) : RecyclerView.ViewHolder(binding.root)
}

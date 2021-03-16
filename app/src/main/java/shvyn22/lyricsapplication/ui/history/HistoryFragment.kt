package shvyn22.lyricsapplication.ui.history

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import shvyn22.lyricsapplication.R
import shvyn22.lyricsapplication.data.model.HistoryItem
import shvyn22.lyricsapplication.databinding.FragmentHistoryBinding

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history), HistoryAdapter.OnItemClickListener {

    private val viewModel : HistoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHistoryBinding.bind(view)

        val historyAdapter = HistoryAdapter(this)

        binding.rvHistory.apply {
            adapter = historyAdapter
            setHasFixedSize(true)

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                    target: RecyclerView.ViewHolder) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val item = historyAdapter.currentList[viewHolder.bindingAdapterPosition]
                    viewModel.deleteTrack(item.idTrack)
                }
            }).attachToRecyclerView(this)
        }

        viewModel.items.observe(viewLifecycleOwner) {
            historyAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.historyEvent.collect { event ->
                when (event) {
                    is HistoryViewModel.HistoryEvent.NavigateToDetails -> {
                        val action = HistoryFragmentDirections
                                .actionHistoryFragmentToDetailsActivity(event.track)
                        findNavController().navigate(action)
                    }
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_fragments, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchItem.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> viewModel.deleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(item: HistoryItem) {
        try {
            viewModel.onTrackSelected(item)
        } catch (e : Exception) {
            Toast.makeText(requireActivity(),
                    getString(R.string.text_no_internet), Toast.LENGTH_LONG).show()
        }
    }
}
package shvyn22.lyricsapplication.ui.library

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import shvyn22.lyricsapplication.R
import shvyn22.lyricsapplication.data.model.LibraryItem
import shvyn22.lyricsapplication.databinding.FragmentLibraryBinding
import java.lang.Exception

@AndroidEntryPoint
class LibraryFragment : Fragment(R.layout.fragment_library), LibraryAdapter.OnItemClickListener {

    private val viewModel : LibraryViewModel by viewModels()

    private var _binding : FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLibraryBinding.bind(view)

        val libraryAdapter = LibraryAdapter(this)

        binding.apply {
            rvLibrary.apply {
                adapter = libraryAdapter
                setHasFixedSize(true)

                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                        target: RecyclerView.ViewHolder) = false

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val item = libraryAdapter.currentList[viewHolder.bindingAdapterPosition]
                        viewModel.deleteTrack(item.idTrack)
                    }
                }).attachToRecyclerView(this)
            }

            viewModel.isLoading.observe(viewLifecycleOwner) {
                if (it) progressBar.visibility = View.VISIBLE
                else progressBar.visibility = View.GONE
            }
        }

        viewModel.items.observe(viewLifecycleOwner) {
            libraryAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.libraryEvent.collect { event ->
                when (event) {
                    is LibraryViewModel.LibraryEvent.NavigateToDetails -> {
                        val action = LibraryFragmentDirections
                                .actionLibraryFragmentToDetailsFragment(event.track)
                        findNavController().navigate(action)
                    }
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onItemClick(item: LibraryItem) {
        try {
            viewModel.onTrackSelected(item)
        } catch (e: Exception) {
            Toast.makeText(requireActivity(),
                    getString(R.string.text_no_internet), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_fragments, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    binding.rvLibrary.scrollToPosition(0)
                    viewModel.searchTracks(query)
                }
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> viewModel.deleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
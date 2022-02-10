package shvyn22.flexinglyrics.ui.library

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.databinding.FragmentLibraryBinding
import shvyn22.flexinglyrics.util.MultiViewModelFactory
import shvyn22.flexinglyrics.util.StateEvent
import shvyn22.flexinglyrics.util.collectOnLifecycle
import shvyn22.flexinglyrics.util.singletonComponent
import javax.inject.Inject

class LibraryFragment : Fragment(R.layout.fragment_library) {

    private val viewModel: LibraryViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.singletonComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLibraryBinding.bind(view)

        val libraryAdapter = LibraryAdapter { viewModel.onTrackSelected(it) }

        binding.apply {
            rvLibrary.apply {
                adapter = libraryAdapter
                setHasFixedSize(true)

                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                ) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ) = false

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val item = libraryAdapter.currentList[viewHolder.adapterPosition]
                        viewModel.deleteTrack(item.idTrack)
                    }
                }).attachToRecyclerView(this)
            }

            viewModel.libraryEvent.collectOnLifecycle(viewLifecycleOwner) { event ->
                progressBar.isVisible = event is StateEvent.Loading

                if (event is StateEvent.NavigateToDetails) {
                    findNavController().navigate(
                        LibraryFragmentDirections
                            .actionLibraryFragmentToDetailsFragment(event.track)
                    )
                } else if (event is StateEvent.Error) {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.text_no_internet),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        viewModel.items.observe(viewLifecycleOwner) {
            libraryAdapter.submitList(it)
        }

        setHasOptionsMenu(true)
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
        if (item.itemId == R.id.action_delete) viewModel.deleteAll()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
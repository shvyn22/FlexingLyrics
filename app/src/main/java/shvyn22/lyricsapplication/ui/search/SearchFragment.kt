package shvyn22.lyricsapplication.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import shvyn22.lyricsapplication.R
import shvyn22.lyricsapplication.data.model.Track
import shvyn22.lyricsapplication.databinding.FragmentSearchBinding
import shvyn22.lyricsapplication.util.Resource
import java.lang.Exception

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), SearchAdapter.OnItemClickListener {

    private val viewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSearchBinding.bind(view)

        val searchAdapter = SearchAdapter(this)

        val text = getString(R.string.text_search)
        val spannableString = SpannableString(text)
        spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.colorAccent)),
                55, 61, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.apply {
            tvSearch.text = spannableString

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (query.isNullOrEmpty()) {
                        tvSearch.visibility = View.VISIBLE
                        rvSearch.visibility = View.GONE
                    } else {
                        tvSearch.visibility = View.GONE
                        rvSearch.visibility = View.VISIBLE
                    }
                }

                override fun afterTextChanged(query: Editable?) {}
            })

            etSearch.imeOptions = EditorInfo.IME_ACTION_SEARCH

            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = etSearch.text.toString()
                    viewModel.searchTracks(query)
                    hideKeyboard()
                }
                true
            }

            rvSearch.adapter = searchAdapter
            rvSearch.setHasFixedSize(true)

            viewModel.tracks.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        progressBar.visibility = View.GONE
                        searchAdapter.submitList(it.data)
                    }
                    is Resource.Loading -> progressBar.visibility = View.VISIBLE
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_LONG).show()
                    }
                }

            }

            viewModel.isLoading.observe(viewLifecycleOwner) {
                if (it) progressBar.visibility = View.VISIBLE
                else progressBar.visibility = View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.searchEvent.collect { event ->
                when(event) {
                    is SearchViewModel.SearchEvent.NavigateToDetails -> {
                        val action = SearchFragmentDirections
                                .actionSearchFragmentToDetailsActivity(event.track)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onItemClick(item: Track) {
        try {
            viewModel.onTrackSelected(item)
        } catch (e: Exception) {
            Toast.makeText(requireActivity(),
                    getString(R.string.text_no_internet), Toast.LENGTH_LONG).show()
        }
    }

    private fun hideKeyboard() {
        val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        inputManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
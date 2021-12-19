package shvyn22.flexinglyrics.ui.search

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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.databinding.FragmentSearchBinding
import shvyn22.flexinglyrics.util.Resource
import shvyn22.flexinglyrics.util.StateEvent
import shvyn22.flexinglyrics.util.collectOnLifecycle

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSearchBinding.bind(view)

        val searchAdapter = SearchAdapter { viewModel.onTrackSelected(it) }

        val text = getString(R.string.text_search)
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(requireContext(), R.color.colorAccent)
            ), 55, 61, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.apply {
            tvSearch.text = spannableString

            etSearch.apply {
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        tvSearch.isVisible = query.isNullOrEmpty()
                        rvSearch.isVisible = !query.isNullOrEmpty()
                    }

                    override fun afterTextChanged(query: Editable?) {}
                })

                imeOptions = EditorInfo.IME_ACTION_SEARCH

                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        val query = etSearch.text.toString()
                        viewModel.searchTracks(query)
                        hideKeyboard()
                    }
                    true
                }
            }

            rvSearch.adapter = searchAdapter

            viewModel.tracks.observe(viewLifecycleOwner) {
                progressBar.isVisible = it is Resource.Loading

                if (it is Resource.Success) searchAdapter.submitList(it.data)
                else if (it is Resource.Error) {
                    Toast.makeText(requireContext(), it.msg, Toast.LENGTH_LONG).show()
                }
            }

            viewModel.searchEvent.collectOnLifecycle(viewLifecycleOwner) { event ->
                progressBar.isVisible = event is StateEvent.Loading

                if (event is StateEvent.NavigateToDetails) {
                    val action = SearchFragmentDirections
                        .actionSearchFragmentToDetailsFragment(event.track)
                    findNavController().navigate(action)
                } else if (event is StateEvent.Error) {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.text_no_internet),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun hideKeyboard() {
        val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager
        inputManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
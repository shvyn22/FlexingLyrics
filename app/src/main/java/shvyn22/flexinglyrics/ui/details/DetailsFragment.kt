package shvyn22.flexinglyrics.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.databinding.FragmentDetailsBinding
import shvyn22.flexinglyrics.ui.details.adapter.PagerAdapter
import shvyn22.flexinglyrics.util.*

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailsFragmentArgs>()
    private val viewModel: DetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsBinding.bind(view)

        val track = args.track
        viewModel.init(track)

        val pagerAdapter = PagerAdapter(childFragmentManager, lifecycle)

        binding.apply {
            Glide.with(view)
                .load(BASE_COVER_URL + track.idAlbum.toString())
                .defaultRequests()
                .into(ivDetails)

            tvDetailsTitle.text = track.track
            tvDetailsArtist.text = track.artist
            tvDetailsAlbum.text = track.album

            val fragmentTitles = arrayListOf(
                getString(R.string.tab_lyrics),
                getString(R.string.tab_artist),
                getString(R.string.tab_album)
            )

            val viewPager = vpDetails
            viewPager.adapter = pagerAdapter

            TabLayoutMediator(tlDetails, viewPager) { tab, position ->
                tab.text = fragmentTitles[position]
            }.attach()

            val addTag = getString(R.string.tag_add)
            val removeTag = getString(R.string.tag_remove)

            btnLibrary.setOnClickListener {
                if (it.tag == addTag) viewModel.addToLibrary()
                else viewModel.removeFromLibrary()
            }

            viewModel.isLibraryItem(track.idTrack).observe(viewLifecycleOwner) {
                btnLibrary.apply {
                    if (it) {
                        tag = removeTag
                        setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_library_remove, 0, 0, 0
                        )
                        text = removeTag
                    } else {
                        tag = addTag
                        setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_library_add, 0, 0, 0
                        )
                        text = addTag
                    }
                }
            }

            viewModel.detailsEvent.collectOnLifecycle(viewLifecycleOwner) { event ->
                progressBar.isVisible = event is StateEvent.Loading

                when (event) {
                    is StateEvent.NavigateToDetails -> {
                        findNavController()
                            .navigate(
                                DetailsFragmentDirections
                                    .actionDetailsFragmentSelf(event.track)
                            )
                    }
                    is StateEvent.NavigateToMedia -> {
                        event.url.let {
                            if (it.isEmpty()) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.text_error_link),
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                                startActivity(intent)
                            }
                        }
                    }
                    is StateEvent.Error ->
                        Toast.makeText(
                            requireActivity(),
                            when (event.error) {
                                StateError.ERROR_FETCHING_DATA ->
                                    getString(R.string.text_error_fetching)
                                StateError.ERROR_LOADING_IMAGE ->
                                    getString(R.string.text_error_loading)
                                StateError.ERROR_PERMISSION_NOT_GRANTED ->
                                    getString(R.string.text_error_permission)
                            },
                            Toast.LENGTH_LONG
                        ).show()
                    else -> Unit
                }
            }
        }
    }
}
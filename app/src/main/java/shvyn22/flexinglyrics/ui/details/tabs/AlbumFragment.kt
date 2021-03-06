package shvyn22.flexinglyrics.ui.details.tabs

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.databinding.FragmentAlbumBinding
import shvyn22.flexinglyrics.ui.details.DetailsViewModel
import shvyn22.flexinglyrics.ui.details.adapter.AlbumAdapter
import shvyn22.flexinglyrics.util.Resource
import shvyn22.flexinglyrics.util.StateError
import shvyn22.flexinglyrics.util.defaultRequests

@AndroidEntryPoint
class AlbumFragment : Fragment(R.layout.fragment_album) {

    private val viewModel: DetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val albumAdapter = AlbumAdapter { viewModel.onItemClick(it) }
        val binding = FragmentAlbumBinding.bind(view)

        binding.apply {
            rvAlbum.apply {
                adapter = albumAdapter
                setHasFixedSize(true)
            }

            viewModel.albumInfo.observe(viewLifecycleOwner) {
                progressBar.isVisible = it is Resource.Loading
                tvTracks.isVisible = it is Resource.Success

                if (it is Resource.Success) {
                    it.data.let { albumInfo ->
                        albumAdapter.submitList(albumInfo.tracks)
                        rvAlbum.scheduleLayoutAnimation()

                        Glide.with(view)
                            .load(albumInfo.cover)
                            .defaultRequests()
                            .into(ivAlbum)

                        tvAlbum.text = albumInfo.album
                    }
                } else if (it is Resource.Error)
                    viewModel.onErrorOccurred(StateError.ERROR_FETCHING_DATA)
            }
        }
    }
}
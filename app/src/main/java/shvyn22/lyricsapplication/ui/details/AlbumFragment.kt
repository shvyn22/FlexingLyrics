package shvyn22.lyricsapplication.ui.details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.lyricsapplication.R
import shvyn22.lyricsapplication.databinding.FragmentAlbumBinding
import shvyn22.lyricsapplication.util.Resource
import shvyn22.lyricsapplication.util.defaultRequests

@AndroidEntryPoint
class AlbumFragment : Fragment(R.layout.fragment_album) {

    private val viewModel: DetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val albumAdapter = AlbumAdapter {
            try {
                viewModel.onItemClick(it)
            } catch (e: Exception) {
                viewModel.onErrorOccurred()
            }
        }
        val binding = FragmentAlbumBinding.bind(view)

        binding.apply {
            rvAlbum.apply {
                adapter = albumAdapter
                setHasFixedSize(true)
            }

            viewModel.albumInfo.observe(viewLifecycleOwner) {
                progressBar.isVisible = it is Resource.Loading

                if (it is Resource.Success) {
                    val albumInfo = it.data
                    albumAdapter.submitList(albumInfo?.tracks)
                    Glide.with(root)
                        .load(albumInfo?.cover)
                        .defaultRequests()
                        .into(ivAlbum)

                    tvAlbum.text = albumInfo?.album
                } else if (it is Resource.Error) viewModel.onErrorOccurred()
            }
        }
    }
}
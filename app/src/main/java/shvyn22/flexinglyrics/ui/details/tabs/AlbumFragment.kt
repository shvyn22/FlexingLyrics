package shvyn22.flexinglyrics.ui.details.tabs

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.databinding.FragmentAlbumBinding
import shvyn22.flexinglyrics.ui.details.DetailsViewModel
import shvyn22.flexinglyrics.ui.details.adapter.AlbumAdapter
import shvyn22.flexinglyrics.util.MultiViewModelFactory
import shvyn22.flexinglyrics.util.Resource
import shvyn22.flexinglyrics.util.defaultRequests
import shvyn22.flexinglyrics.util.singletonComponent
import javax.inject.Inject

class AlbumFragment : Fragment(R.layout.fragment_album) {

    private val viewModel: DetailsViewModel by activityViewModels { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.singletonComponent.inject(this)
    }

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

                if (it is Resource.Success) {
                    it.data.let { albumInfo ->
                        albumAdapter.submitList(albumInfo.tracks)

                        Glide.with(view)
                            .load(albumInfo.cover)
                            .defaultRequests()
                            .into(ivAlbum)

                        tvAlbum.text = albumInfo.album
                    }
                } else if (it is Resource.Error) viewModel.onErrorOccurred()
            }
        }
    }
}
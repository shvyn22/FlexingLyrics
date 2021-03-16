package shvyn22.lyricsapplication.ui.details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.lyricsapplication.LyricsApplication.Companion.ERROR_FETCHING_DATA
import shvyn22.lyricsapplication.R
import shvyn22.lyricsapplication.data.model.AlbumInfo
import shvyn22.lyricsapplication.databinding.FragmentAlbumBinding
import shvyn22.lyricsapplication.util.Resource
import java.lang.Exception

@AndroidEntryPoint
class AlbumFragment : Fragment(R.layout.fragment_album), AlbumAdapter.OnItemClickListener {

    private val viewModel: DetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAlbumInfo(viewModel.track?.idArtist!!, viewModel.track?.idAlbum!!)

        val albumAdapter = AlbumAdapter(this)
        val binding = FragmentAlbumBinding.bind(view)

        binding.apply {
            rvAlbum.apply {
                adapter = albumAdapter
                setHasFixedSize(true)
            }

            viewModel.albumInfo.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        progressBar.visibility = View.GONE
                        val albumInfo = it.data
                        albumAdapter.submitList(albumInfo?.tracks)
                        Glide.with(root)
                                .load(albumInfo?.cover)
                                .fitCenter()
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .error(R.drawable.ic_error)
                                .into(ivAlbum)

                        tvAlbum.text = albumInfo?.album
                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(),
                                ERROR_FETCHING_DATA, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onItemClick(item: AlbumInfo.TrackInfo) {
        try {
            viewModel.onItemClick(item)
        } catch (e: Exception) {
            Toast.makeText(requireActivity(),
                    getString(R.string.text_no_internet), Toast.LENGTH_LONG).show()
        }
    }
}
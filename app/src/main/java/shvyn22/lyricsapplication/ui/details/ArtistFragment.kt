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
import shvyn22.lyricsapplication.databinding.FragmentArtistBinding
import shvyn22.lyricsapplication.util.Resource

@AndroidEntryPoint
class ArtistFragment : Fragment(R.layout.fragment_artist) {

    private val viewModel: DetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getArtistInfo(viewModel.track?.idArtist!!)

        val binding = FragmentArtistBinding.bind(view)

        binding.apply {
            viewModel.artistInfo.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        progressBar.visibility = View.GONE
                        it.data?.let { artistInfo ->
                            Glide.with(root)
                                    .load(artistInfo.cover)
                                    .fitCenter()
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .error(R.drawable.ic_error)
                                    .into(ivArtist)

                            tvArtist.text = artistInfo.artist
                            tvArtistCountry.text = getString(R.string.text_country, artistInfo.country)
                            tvArtistGender.text = getString(R.string.text_gender, artistInfo.gender)

                            ivYoutube.setOnClickListener { viewModel.onMediaIconClick(artistInfo.youtube) }
                            ivInstagram.setOnClickListener { viewModel.onMediaIconClick(artistInfo.instagram) }
                            ivFacebook.setOnClickListener { viewModel.onMediaIconClick(artistInfo.facebook) }
                            ivTwitter.setOnClickListener { viewModel.onMediaIconClick(artistInfo.twitter) }
                            ivWeb.setOnClickListener { viewModel.onMediaIconClick(artistInfo.website) }
                        }
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
}
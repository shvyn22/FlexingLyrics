package shvyn22.lyricsapplication.ui.details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.lyricsapplication.R
import shvyn22.lyricsapplication.databinding.FragmentArtistBinding
import shvyn22.lyricsapplication.util.Resource
import shvyn22.lyricsapplication.util.defaultRequests

@AndroidEntryPoint
class ArtistFragment : Fragment(R.layout.fragment_artist) {

    private val viewModel: DetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentArtistBinding.bind(view)

        binding.apply {
            viewModel.artistInfo.observe(viewLifecycleOwner) {
                progressBar.isVisible = it is Resource.Loading

                if (it is Resource.Success) {
                    it.data?.let { artistInfo ->
                        Glide.with(root)
                            .load(artistInfo.cover)
                            .defaultRequests()
                            .into(ivArtist)

                        tvArtist.text = artistInfo.artist
                        tvArtistCountry.text = getString(
                            R.string.text_country,
                            artistInfo.country
                        )
                        tvArtistGender.text = getString(
                            R.string.text_gender,
                            artistInfo.gender
                        )
                        ivYoutube.setOnClickListener {
                            viewModel.onMediaIconClick(artistInfo.youtube)
                        }
                        ivInstagram.setOnClickListener {
                            viewModel.onMediaIconClick(artistInfo.instagram)
                        }
                        ivFacebook.setOnClickListener {
                            viewModel.onMediaIconClick(artistInfo.facebook)
                        }
                        ivTwitter.setOnClickListener {
                            viewModel.onMediaIconClick(artistInfo.twitter)
                        }
                        ivWeb.setOnClickListener {
                            viewModel.onMediaIconClick(artistInfo.website)
                        }
                    }
                } else if (it is Resource.Error) viewModel.onErrorOccurred()
            }
        }
    }
}
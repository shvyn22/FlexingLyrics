package shvyn22.flexinglyrics.ui.details.tabs

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.databinding.FragmentArtistBinding
import shvyn22.flexinglyrics.ui.details.DetailsViewModel
import shvyn22.flexinglyrics.util.MultiViewModelFactory
import shvyn22.flexinglyrics.util.Resource
import shvyn22.flexinglyrics.util.defaultRequests
import shvyn22.flexinglyrics.util.singletonComponent
import javax.inject.Inject

class ArtistFragment : Fragment(R.layout.fragment_artist) {

    private val viewModel: DetailsViewModel by activityViewModels { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.singletonComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentArtistBinding.bind(view)

        binding.apply {
            viewModel.getArtistInfo().observe(viewLifecycleOwner) {
                progressBar.isVisible = it is Resource.Loading

                if (it is Resource.Success) {
                    it.data.let { artistInfo ->
                        Glide.with(view)
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
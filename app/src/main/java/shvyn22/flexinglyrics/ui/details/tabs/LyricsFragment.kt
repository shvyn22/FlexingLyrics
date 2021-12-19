package shvyn22.flexinglyrics.ui.details.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.databinding.FragmentLyricsBinding
import shvyn22.flexinglyrics.ui.details.DetailsViewModel

@AndroidEntryPoint
class LyricsFragment : Fragment(R.layout.fragment_lyrics) {

    private val viewModel: DetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLyricsBinding.bind(view)

        binding.tvLyrics.text = viewModel.track.lyrics ?: getString(R.string.text_no_lyrics)
    }
}
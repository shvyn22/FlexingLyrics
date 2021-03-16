package shvyn22.lyricsapplication.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.lyricsapplication.R
import shvyn22.lyricsapplication.databinding.FragmentLyricsBinding

@AndroidEntryPoint
class LyricsFragment : Fragment(R.layout.fragment_lyrics) {

    private val viewModel: DetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLyricsBinding.bind(view)

        val lyrics = viewModel.track?.lyrics

        binding.tvLyrics.text = lyrics ?: getString(R.string.text_no_lyrics)
    }
}
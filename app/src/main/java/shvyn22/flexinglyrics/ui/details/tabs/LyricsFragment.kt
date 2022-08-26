package shvyn22.flexinglyrics.ui.details.tabs

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.databinding.FragmentLyricsBinding
import shvyn22.flexinglyrics.service.BitmapDownloadService
import shvyn22.flexinglyrics.ui.details.DetailsViewModel
import shvyn22.flexinglyrics.util.StateError
import shvyn22.flexinglyrics.util.WORK_TEXT_KEY
import shvyn22.flexinglyrics.util.WORK_TITLE_KEY

@AndroidEntryPoint
class LyricsFragment : Fragment(R.layout.fragment_lyrics) {

    private val viewModel: DetailsViewModel by activityViewModels()

    private val requestPermission = registerForActivityResult(RequestPermission()) { isGranted ->
        if (isGranted)
            initWork(
                title = viewModel.track.track,
                text = viewModel.track.lyrics
            )
        else
            viewModel.onErrorOccurred(StateError.ERROR_PERMISSION_NOT_GRANTED)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLyricsBinding.bind(view)

        binding.apply {
            tvLyrics.text = viewModel.track.lyrics ?: getString(R.string.text_error_lyrics)
            ivDownload.isVisible = viewModel.track.lyrics != null

            ivDownload.setOnClickListener {
                requestPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun initWork(
        title: String,
        text: String?
    ) {
        Intent(activity, BitmapDownloadService::class.java).also {
            it.putExtra(WORK_TITLE_KEY, title)
            it.putExtra(WORK_TEXT_KEY, text)
            activity?.startService(it)
        }
    }
}
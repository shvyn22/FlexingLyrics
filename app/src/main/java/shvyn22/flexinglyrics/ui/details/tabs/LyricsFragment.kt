package shvyn22.flexinglyrics.ui.details.tabs

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.work.*
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.databinding.FragmentLyricsBinding
import shvyn22.flexinglyrics.ui.details.DetailsViewModel
import shvyn22.flexinglyrics.util.StateError
import shvyn22.flexinglyrics.util.WORKER_TEXT_KEY
import shvyn22.flexinglyrics.util.WORKER_TITLE_KEY
import shvyn22.flexinglyrics.work.BitmapWorker
import shvyn22.flexinglyrics.work.DownloadWorker

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
        val workManager = WorkManager
            .getInstance(requireContext())
            .beginWith(
                OneTimeWorkRequestBuilder<BitmapWorker>()
                    .setInputData(workDataOf(WORKER_TEXT_KEY to text))
                    .build()
            )
            .then(
                OneTimeWorkRequestBuilder<DownloadWorker>()
                    .setInputData(workDataOf(WORKER_TITLE_KEY to title))
                    .build()
            )
        workManager.enqueue()
        workManager.workInfosLiveData.observe(viewLifecycleOwner) { infos ->
            infos.forEachIndexed { index, info ->
                if (info.state.isFinished) {
                    if (info.state == WorkInfo.State.FAILED) {
                        viewModel.onErrorOccurred(StateError.ERROR_LOADING_IMAGE)
                    } else if (info.state == WorkInfo.State.SUCCEEDED && index == 1) {
                        Toast
                            .makeText(
                                requireContext(),
                                getString(R.string.text_success_loading),
                                Toast.LENGTH_LONG
                            )
                            .show()
                    }
                }
            }
        }
    }
}
package shvyn22.lyricsapplication.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import shvyn22.lyricsapplication.LyricsApplication.Companion.BASE_COVER_URL
import shvyn22.lyricsapplication.R
import shvyn22.lyricsapplication.databinding.FragmentDetailsBinding

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailsFragmentArgs>()
    private val viewModel: DetailsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsBinding.bind(view)
        val track = args.track
        viewModel.init(track)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLibraryItem(track.idTrack)
            viewModel.detailsEvent.collect { event ->
                when (event) {
                    is DetailsViewModel.DetailsEvent.NavigateToDetails -> {
                        findNavController().navigate(DetailsFragmentDirections
                                .actionDetailsFragmentSelf(event.track))
                    }
                    is DetailsViewModel.DetailsEvent.NavigateToMedia -> {
                        val url = event.url
                        if (url.isEmpty()) {
                            Toast.makeText(requireContext(),
                                    getString(R.string.text_no_link), Toast.LENGTH_LONG).show()
                        } else {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        }
                    }
                }
            }
        }

        val pagerAdapter = PagerAdapter(childFragmentManager, lifecycle)

        viewModel.addToHistory(track)

        binding.apply {
            Glide.with(root)
                .load(BASE_COVER_URL + track.idAlbum.toString())
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(ivDetails)

            tvDetailsTitle.text = track.track
            tvDetailsArtist.text = track.artist
            tvDetailsAlbum.text = track.album

            btnLibrary.setOnClickListener {
                viewModel.onToggleLibrary(track)
            }

            val fragmentTitles = arrayListOf(
                getString(R.string.tab_lyrics),
                getString(R.string.tab_artist),
                getString(R.string.tab_album)
            )
            val viewPager = binding.vpDetails
            viewPager.adapter = pagerAdapter

            TabLayoutMediator(binding.tlDetails, viewPager) { tab, position ->
                tab.text = fragmentTitles[position]
            }.attach()
        }

        val addTag = getString(R.string.tag_add)
        val addDrawable = R.drawable.ic_library_add
        val removeTag = getString(R.string.tag_remove)
        val removeDrawable = R.drawable.ic_library_remove

        viewModel.isLibraryItem.observe(viewLifecycleOwner) {
            if (it) {
                binding.btnLibrary.apply {
                    tag = removeTag
                    setCompoundDrawablesWithIntrinsicBounds(
                            removeDrawable, 0, 0, 0)
                    text = removeTag
                }
            } else {
                binding.btnLibrary.apply {
                    tag = addTag
                    setCompoundDrawablesWithIntrinsicBounds(
                            addDrawable, 0, 0, 0)
                    text = addTag
                }
            }
        }
    }
}
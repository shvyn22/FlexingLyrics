package shvyn22.lyricsapplication.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import shvyn22.lyricsapplication.LyricsApplication.Companion.BASE_COVER_URL
import shvyn22.lyricsapplication.R
import shvyn22.lyricsapplication.databinding.ActivityDetailsBinding

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private lateinit var viewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        val binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getParcelableExtra(TRACK_KEY) ?: args.track
        viewModel.init(track)

        lifecycleScope.launchWhenStarted {
            viewModel.isLibraryItem(track.idTrack)
            viewModel.detailsEvent.collect { event ->
                when (event) {
                    is DetailsViewModel.DetailsEvent.NavigateToDetails -> {
                        val intent = Intent(this@DetailsActivity, DetailsActivity::class.java)
                        intent.putExtra(TRACK_KEY, event.track)
                        startActivity(intent)
                    }
                    is DetailsViewModel.DetailsEvent.NavigateToMedia -> {
                        val url = event.url
                        if (url.isEmpty()) {
                            Toast.makeText(this@DetailsActivity,
                                    getString(R.string.text_no_link), Toast.LENGTH_LONG).show()
                        } else {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        }
                    }
                }
            }
        }

        val pagerAdapter = PagerAdapter(supportFragmentManager, lifecycle)

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

        viewModel.isLibraryItem.observe(this, {
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
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    companion object {
        const val TRACK_KEY = "track"
    }
}
package shvyn22.flexinglyrics.ui.details.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import shvyn22.flexinglyrics.ui.details.tabs.LyricsFragment
import shvyn22.flexinglyrics.ui.details.tabs.AlbumFragment
import shvyn22.flexinglyrics.ui.details.tabs.ArtistFragment

class PagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var fragments = arrayListOf(
        LyricsFragment(),
        ArtistFragment(),
        AlbumFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
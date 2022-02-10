package shvyn22.flexinglyrics.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import shvyn22.flexinglyrics.di.module.AppModule
import shvyn22.flexinglyrics.ui.details.DetailsFragment
import shvyn22.flexinglyrics.ui.details.tabs.AlbumFragment
import shvyn22.flexinglyrics.ui.details.tabs.ArtistFragment
import shvyn22.flexinglyrics.ui.details.tabs.LyricsFragment
import shvyn22.flexinglyrics.ui.history.HistoryFragment
import shvyn22.flexinglyrics.ui.library.LibraryFragment
import shvyn22.flexinglyrics.ui.search.SearchFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface SingletonComponent {

    fun inject(searchFragment: SearchFragment)

    fun inject(libraryFragment: LibraryFragment)

    fun inject(historyFragment: HistoryFragment)

    fun inject(detailsFragment: DetailsFragment)

    fun inject(albumFragment: AlbumFragment)

    fun inject(artistFragment: ArtistFragment)

    fun inject(lyricsFragment: LyricsFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): SingletonComponent
    }
}
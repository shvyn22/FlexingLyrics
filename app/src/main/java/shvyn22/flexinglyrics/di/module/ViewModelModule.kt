package shvyn22.flexinglyrics.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import shvyn22.flexinglyrics.di.util.ViewModelKey
import shvyn22.flexinglyrics.ui.details.DetailsViewModel
import shvyn22.flexinglyrics.ui.history.HistoryViewModel
import shvyn22.flexinglyrics.ui.library.LibraryViewModel
import shvyn22.flexinglyrics.ui.search.SearchViewModel

@Module
interface ViewModelModule {

    @Binds
    @[IntoMap ViewModelKey(SearchViewModel::class)]
    fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(LibraryViewModel::class)]
    fun bindLibraryViewModel(libraryViewModel: LibraryViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(HistoryViewModel::class)]
    fun bindHistoryViewModel(historyViewModel: HistoryViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(DetailsViewModel::class)]
    fun bindsDetailsViewModel(detailsViewModel: DetailsViewModel): ViewModel
}
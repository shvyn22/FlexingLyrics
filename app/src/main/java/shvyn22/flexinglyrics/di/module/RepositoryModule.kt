package shvyn22.flexinglyrics.di.module

import dagger.Module
import dagger.Provides
import shvyn22.flexinglyrics.api.ApiInterface
import shvyn22.flexinglyrics.data.local.dao.HistoryDao
import shvyn22.flexinglyrics.data.local.dao.LibraryDao
import shvyn22.flexinglyrics.repository.local.HistoryRepository
import shvyn22.flexinglyrics.repository.local.HistoryRepositoryImpl
import shvyn22.flexinglyrics.repository.local.LibraryRepository
import shvyn22.flexinglyrics.repository.local.LibraryRepositoryImpl
import shvyn22.flexinglyrics.repository.remote.RemoteRepository
import shvyn22.flexinglyrics.repository.remote.RemoteRepositoryImpl
import javax.inject.Singleton

@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRemoteRepository(
        apiInterface: ApiInterface
    ): RemoteRepository = RemoteRepositoryImpl(apiInterface)

    @Provides
    @Singleton
    fun provideHistoryRepository(
        historyDao: HistoryDao
    ): HistoryRepository = HistoryRepositoryImpl(historyDao)

    @Provides
    @Singleton
    fun provideLibraryRepository(
        libraryDao: LibraryDao
    ): LibraryRepository = LibraryRepositoryImpl(libraryDao)
}
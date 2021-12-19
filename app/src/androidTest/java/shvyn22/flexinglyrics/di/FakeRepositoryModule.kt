package shvyn22.flexinglyrics.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import shvyn22.flexinglyrics.api.FakeApiImpl
import shvyn22.flexinglyrics.data.local.dao.HistoryDao
import shvyn22.flexinglyrics.data.local.dao.LibraryDao
import shvyn22.flexinglyrics.repository.FakeHistoryRepositoryImpl
import shvyn22.flexinglyrics.repository.FakeLibraryRepositoryImpl
import shvyn22.flexinglyrics.repository.FakeRemoteRepositoryImpl
import shvyn22.flexinglyrics.repository.local.HistoryRepository
import shvyn22.flexinglyrics.repository.local.LibraryRepository
import shvyn22.flexinglyrics.repository.remote.RemoteRepository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object FakeRepositoryModule {

    @Provides
    @Singleton
    fun provideRemoteRepository(): RemoteRepository = FakeRemoteRepositoryImpl(FakeApiImpl())

    @Provides
    @Singleton
    fun provideHistoryRepository(
        historyDao: HistoryDao
    ): HistoryRepository = FakeHistoryRepositoryImpl(historyDao)

    @Provides
    @Singleton
    fun provideLibraryRepository(
        libraryDao: LibraryDao
    ): LibraryRepository = FakeLibraryRepositoryImpl(libraryDao)
}
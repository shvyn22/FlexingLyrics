package shvyn22.lyricsapplication.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import shvyn22.lyricsapplication.data.local.AppDatabase
import shvyn22.lyricsapplication.data.local.dao.HistoryDao
import shvyn22.lyricsapplication.data.local.dao.LibraryDao
import shvyn22.lyricsapplication.repository.FakeRepositoryImpl
import shvyn22.lyricsapplication.repository.Repository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    fun provideDatabase(app: Application): AppDatabase =
        Room
            .inMemoryDatabaseBuilder(app, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

    @Provides
    fun provideHistoryDao(db: AppDatabase): HistoryDao =
        db.historyDao()

    @Provides
    fun provideLibraryDao(db: AppDatabase): LibraryDao =
        db.libraryDao()

    @Provides
    @Singleton
    fun provideRepository(
        historyDao: HistoryDao,
        libraryDao: LibraryDao
    ): Repository = FakeRepositoryImpl(
        historyDao = historyDao,
        libraryDao = libraryDao
    )
}
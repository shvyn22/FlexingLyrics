package shvyn22.flexinglyrics.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import shvyn22.flexinglyrics.data.local.AppDatabase
import shvyn22.flexinglyrics.data.local.dao.HistoryDao
import shvyn22.flexinglyrics.data.local.dao.LibraryDao

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object FakeDatabaseModule {

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
}
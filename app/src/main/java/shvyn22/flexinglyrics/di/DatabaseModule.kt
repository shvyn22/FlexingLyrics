package shvyn22.flexinglyrics.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import shvyn22.flexinglyrics.data.local.AppDatabase
import shvyn22.flexinglyrics.data.local.dao.HistoryDao
import shvyn22.flexinglyrics.data.local.dao.LibraryDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(app: Application): AppDatabase =
        Room
            .databaseBuilder(app, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideHistoryDao(db: AppDatabase): HistoryDao =
        db.historyDao()

    @Provides
    fun provideLibraryDao(db: AppDatabase): LibraryDao =
        db.libraryDao()
}
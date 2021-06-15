package shvyn22.lyricsapplication.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import shvyn22.lyricsapplication.api.ApiInterface
import shvyn22.lyricsapplication.data.local.AppDatabase
import shvyn22.lyricsapplication.data.local.dao.HistoryDao
import shvyn22.lyricsapplication.data.local.dao.LibraryDao
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(ApiInterface.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

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
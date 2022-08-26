package shvyn22.flexinglyrics.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import shvyn22.flexinglyrics.util.SERVICE_SCOPE_NAME
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @Singleton
    @Provides
    @Named(SERVICE_SCOPE_NAME)
    fun provideServiceScope(): CoroutineScope = CoroutineScope(Job() + Dispatchers.Main)
}
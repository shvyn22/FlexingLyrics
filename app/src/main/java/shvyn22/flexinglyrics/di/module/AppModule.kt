package shvyn22.flexinglyrics.di.module

import dagger.Module

@Module(includes = [
        DatabaseModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
    ]
)
object AppModule
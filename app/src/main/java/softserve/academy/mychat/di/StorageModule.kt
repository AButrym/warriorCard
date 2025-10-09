package softserve.academy.mychat.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import softserve.academy.mychat.FileStorageService
import softserve.academy.mychat.StorageService


@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun provideStorageService(
        @ApplicationContext context: Context
    ): StorageService = FileStorageService(context)
}
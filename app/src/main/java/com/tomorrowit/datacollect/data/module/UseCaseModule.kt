package com.tomorrowit.datacollect.data.module

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import com.tomorrowit.datacollect.domain.usecases.GetCollectedDataListUseCase
import com.tomorrowit.datacollect.domain.usecases.GetGeneralDataUseCase
import com.tomorrowit.datacollect.domain.usecases.GetSoftwareUseCase
import com.tomorrowit.datacollect.domain.usecases.OpenUrlInBrowserUseCase
import com.tomorrowit.datacollect.domain.usecases.PreferenceValueUseCase
import com.tomorrowit.datacollect.domain.usecases.ReadAllApplicationsUseCase
import com.tomorrowit.datacollect.utils.extensions.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun providePackageManager(application: Application): PackageManager {
        return application.packageManager
    }

    @Provides
    fun provideGetCollectedDataListUseCase(): GetCollectedDataListUseCase {
        return GetCollectedDataListUseCase()
    }

    @Provides
    fun provideReadAllApplicationsUseCase(
        packageManager: PackageManager,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): ReadAllApplicationsUseCase {
        return ReadAllApplicationsUseCase(
            packageManager, coroutineDispatcher
        )
    }

    @Provides
    fun provideOpenUrlInBrowserUseCase(): OpenUrlInBrowserUseCase {
        return OpenUrlInBrowserUseCase()
    }

    @Provides
    fun provideGetGeneralDataUseCase(
        @ApplicationContext context: Context,
        @MainDispatcher coroutineDispatcher: CoroutineDispatcher
    ): GetGeneralDataUseCase {
        return GetGeneralDataUseCase(context, coroutineDispatcher)
    }

    @Provides
    fun provideGetSoftwareUseCase(
        @ApplicationContext context: Context,
        @MainDispatcher coroutineDispatcher: CoroutineDispatcher
    ): GetSoftwareUseCase {
        return GetSoftwareUseCase(context, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun providePreferenceValueUseCase(
        @ApplicationContext context: Context
    ): PreferenceValueUseCase {
        return PreferenceValueUseCase(context.dataStore)
    }

}
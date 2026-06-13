package com.vcco.weather.di

import com.vcco.weather.network.service.OpenWeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import org.mockito.Mockito

@DisableInstallInCheck
@Module
object FakeApplicationModule {

    @Provides
    fun provideOpenWeatherService(): OpenWeatherService =
        Mockito.mock(OpenWeatherService::class.java)
}

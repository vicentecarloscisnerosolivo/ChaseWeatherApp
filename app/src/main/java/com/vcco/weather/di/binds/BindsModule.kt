package com.vcco.weather.di.binds

import com.vcco.weather.network.apiHelper.OpenWeatherApiHelper
import com.vcco.weather.network.apiHelper.OpenWeatherApiHelperImp
import com.vcco.weather.network.service.OpenWeatherService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {

    /**
     * Bind OpenWeatherApi interface and Implementation
     */
    @Binds
    abstract fun provideApiHelper(apiHelper: OpenWeatherApiHelperImp): OpenWeatherApiHelper
}
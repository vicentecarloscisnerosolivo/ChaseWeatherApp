package com.vcco.weather.di.module

import android.content.Context
import com.vcco.weather.BuildConfig
import com.vcco.weather.data.Preference
import com.vcco.weather.network.service.OpenWeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    /**
     * Provides unique instance of OKHttp logger Interceptor to trace network calls
     *
     * Singleton
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    /**
     * Provide an unique instance of Retrofit to network calls
     *
     * Singleton
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()

    /**
     * Provide service from OpenWeatherService to do api Call
     *
     * Singleton
     */
    @Provides
    @Singleton
    fun provideOpenWeatherService(retrofit: Retrofit) =
        retrofit.create(OpenWeatherService::class.java)


    /**
     * Provide object from Prefence to use Shared Preference
     *
     * Singleton
     */
    @Provides
    @Singleton
    fun providePreference(@ApplicationContext context: Context) =
        Preference(context)


}
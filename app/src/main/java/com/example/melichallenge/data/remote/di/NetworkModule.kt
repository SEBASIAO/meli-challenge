package com.example.melichallenge.data.remote.di

import com.example.melichallenge.data.remote.datasource.SearchRemoteDataSource
import com.example.melichallenge.data.remote.services.SearchApiServices
import com.example.melichallenge.data.repository.SearchRepositoryImpl
import com.example.melichallenge.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.mercadolibre.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideSearchApiServices(retrofit: Retrofit): SearchApiServices {
        return retrofit.create(SearchApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchRemoteDataSource(services: SearchApiServices): SearchRemoteDataSource =
        SearchRemoteDataSource(services)

    @Provides
    @Singleton
    fun provideSearchRepository(remoteDataSource: SearchRemoteDataSource): SearchRepository =
        SearchRepositoryImpl(remoteDataSource)
}
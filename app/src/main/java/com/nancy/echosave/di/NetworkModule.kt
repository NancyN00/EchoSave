package com.nancy.echosave.di

import com.nancy.echosave.data.ElevenLabsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideElevenLabsApi(): ElevenLabsApi {
        return Retrofit.Builder()
            .baseUrl("https://api.elevenlabs.io/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ElevenLabsApi::class.java)
    }
}
package com.genzsage.core.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object HiltNetworkModule {




    @Singleton
    @Provides
    @BaseUrl
    fun provideUrl(): String{
        return "https://genzsagebackend.onrender.com"
    }

    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    // --- BASE CLIENT (Unauthenticated) ---
    // Used for Login, Register, and by the AuthInterceptor to call Refresh
    @Provides
    @Singleton
    @UnauthenticatedClient
    fun provideBaseOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @UnauthenticatedClient
    fun provideAuthRetrofit(
        @UnauthenticatedClient okHttpClient: OkHttpClient,
        json: Json,
        @BaseUrl baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(@UnauthenticatedClient retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    // --- MAIN CLIENT (Authenticated) ---
    // Used for Feed, Profile, Post, etc.
    @Provides
    @Singleton
    @AuthenticatedClient
    fun provideAuthenticatedOkHttpClient(
        @UnauthenticatedClient baseClient: OkHttpClient, // Inherit from Base!
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        // newBuilder() shares the Connection Pool and Dispatcher with the base client!
        return baseClient.newBuilder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @AuthenticatedClient
    fun provideMainRetrofit(
        @AuthenticatedClient okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.genzsage.com/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }



}
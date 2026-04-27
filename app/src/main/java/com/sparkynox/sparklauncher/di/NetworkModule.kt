package com.sparkynox.sparklauncher.di

import com.sparkynox.sparklauncher.BuildConfig
import com.sparkynox.sparklauncher.data.api.CurseForgeApiService
import com.sparkynox.sparklauncher.data.api.ModrinthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BASIC
            else
                HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val req = chain.request().newBuilder()
                    .header("User-Agent", ModrinthApiService.USER_AGENT)
                    .build()
                chain.proceed(req)
            }
            .build()
    }

    @Singleton
    @Provides
    @Named("modrinth")
    fun provideModrinthRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ModrinthApiService.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    @Named("curseforge")
    fun provideCurseForgeRetrofit(client: OkHttpClient): Retrofit {
        val cfClient = client.newBuilder()
            .addInterceptor { chain ->
                val req = chain.request().newBuilder()
                    .header("x-api-key", BuildConfig.CURSEFORGE_API_KEY)
                    .header("Accept", "application/json")
                    .build()
                chain.proceed(req)
            }
            .build()
        return Retrofit.Builder()
            .baseUrl(CurseForgeApiService.BASE_URL)
            .client(cfClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideModrinthApi(@Named("modrinth") retrofit: Retrofit): ModrinthApiService =
        retrofit.create(ModrinthApiService::class.java)

    @Singleton
    @Provides
    fun provideCurseForgeApi(@Named("curseforge") retrofit: Retrofit): CurseForgeApiService =
        retrofit.create(CurseForgeApiService::class.java)
}

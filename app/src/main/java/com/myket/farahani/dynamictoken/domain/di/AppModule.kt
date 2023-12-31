package com.myket.farahani.dynamictoken.domain.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.myket.farahani.dynamictoken.data.remote.ApiConstants.BASE_URL
import com.myket.farahani.dynamictoken.data.remote.ApiService
import com.myket.farahani.dynamictoken.data.remote.FileDownloadApi
import com.myket.farahani.dynamictoken.domain.repository.Repository
import com.myket.farahani.dynamictoken.domain.repository.RepositoryImpl
import com.myket.farahani.dynamictoken.domain.use_case.*
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
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideApiService(client: OkHttpClient, gson: Gson): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFileDownloadApiService(client: OkHttpClient, gson: Gson): FileDownloadApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        return retrofit.create(FileDownloadApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(
        api: ApiService,
        downloadApi: FileDownloadApi
    ): Repository {
        return RepositoryImpl(api, downloadApi)
    }

    @Provides
    @Singleton
    fun provideDynamicTokenUseCase(
        repository: Repository
    ): DynamicTokenUseCase {
        return DynamicTokenUseCase(
            getCalcData = GetCalcData(repository),
            getToken = GetToken(repository),
            getAppData = GetAppData(repository),
            downloadFile = DownloadFile(repository)
        )
    }
}
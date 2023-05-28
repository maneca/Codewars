package com.example.codewars.di

import android.app.Application
import com.example.codewars.BuildConfig
import com.example.codewars.data.local.CodewarsDatabase
import com.example.codewars.data.remote.CodewarsApi
import com.example.codewars.data.repository.CodewarsRepositoryImp
import com.example.codewars.domain.repository.CodewarsRepository
import com.example.codewars.utils.BASE_URL
import com.example.codewars.utils.DefaultDispatcherProvider
import com.example.codewars.utils.DispatcherProvider
import retrofit2.converter.gson.GsonConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class CodewarsModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application)
            = CodewarsDatabase.invoke(application)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient{
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideChallengesApi(client: OkHttpClient): CodewarsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(CodewarsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCodewarsRepository(
        api: CodewarsApi,
        database: CodewarsDatabase
    ) : CodewarsRepository {
        return CodewarsRepositoryImp(api = api, database = database)
    }

    @Provides
    @Singleton
    fun providesDispatcher(): DispatcherProvider = DefaultDispatcherProvider()

}
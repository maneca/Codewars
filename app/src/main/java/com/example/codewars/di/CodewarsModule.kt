package com.example.codewars.di

import android.app.Application
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
import retrofit2.Retrofit
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
    fun provideChallengesApi(): CodewarsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
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
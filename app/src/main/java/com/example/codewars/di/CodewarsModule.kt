package com.example.codewars.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.codewars.R
import com.example.codewars.data.local.CodewarsDatabase
import com.example.codewars.data.remote.CodewarsApi
import com.example.codewars.data.repository.CodewarsRepositoryImp
import com.example.codewars.domain.repository.CodewarsRepository
import com.example.codewars.utils.DefaultDispatcherProvider
import com.example.codewars.utils.DispatcherProvider
import retrofit2.converter.gson.GsonConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class CodewarsModule {

    @Provides
    @Singleton
    fun provideCodewarsDatabase(@ApplicationContext context: Context): CodewarsDatabase {

        return Room.databaseBuilder(context, CodewarsDatabase::class.java, "codewars_db")
            .build()
    }

    @Provides
    @Singleton
    fun provideChallengesApi(application: Application): CodewarsApi {
        return Retrofit.Builder()
            .baseUrl(application.applicationContext.getString(R.string.BASE_URL))
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
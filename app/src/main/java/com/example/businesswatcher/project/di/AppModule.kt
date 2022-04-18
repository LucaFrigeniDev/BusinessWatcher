package com.example.businesswatcher.project.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.businesswatcher.project.App
import com.example.businesswatcher.project.data.BusinessWatcherDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApp(@ApplicationContext context: Context): App = context as App

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        BusinessWatcherDataBase::class.java,
        "businesswatcher_database"
    ).build()

    @Singleton
    @Provides
    fun provideBusinessSectorDao(db: BusinessWatcherDataBase) = db.businessSectorDao()

    @Singleton
    @Provides
    fun provideGroupDao(db: BusinessWatcherDataBase) = db.groupDao()

    @Singleton
    @Provides
    fun provideCompanyDao(db: BusinessWatcherDataBase) = db.companyDao()
}
package com.dnd.safety.di

import android.content.Context
import androidx.room.Room
import com.dnd.safety.data.local.AppDatabase
import com.dnd.safety.data.local.dao.SampleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideSampleDao(
        database: AppDatabase
    ): SampleDao = database.sampleDao()

    private const val DATABASE_NAME = "app_database"
}
package com.dnd.safety.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dnd.safety.data.local.AppDatabase
import com.dnd.safety.data.local.dao.FcmDao
import com.dnd.safety.data.local.dao.SettingDao
import com.dnd.safety.data.local.dao.TopicDao
import com.dnd.safety.data.local.entity.SettingEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun databaseCallBack(): RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            Executors.newSingleThreadExecutor().execute {
                runBlocking {
                    db.execSQL(SettingEntity.INSERT_QUERY)
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        addCallback: RoomDatabase.Callback
    ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
        .addCallback(addCallback)
        .build()

    @Provides
    @Singleton
    fun provideSettingDao(
        database: AppDatabase
    ): SettingDao = database.settingDao()

    @Provides
    @Singleton
    fun provideFcmDao(
        database: AppDatabase
    ): FcmDao = database.fcmDao()

    @Provides
    @Singleton
    fun provideTopicDao(
        database: AppDatabase
    ): TopicDao = database.topicDao()

    private const val DATABASE_NAME = "app_database"
}
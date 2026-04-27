package com.sparkynox.sparklauncher.di

import android.content.Context
import androidx.room.Room
import com.sparkynox.sparklauncher.data.InstanceDao
import com.sparkynox.sparklauncher.data.SparkDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): SparkDatabase =
        Room.databaseBuilder(context, SparkDatabase::class.java, "spark_launcher.db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideInstanceDao(db: SparkDatabase): InstanceDao = db.instanceDao()
}

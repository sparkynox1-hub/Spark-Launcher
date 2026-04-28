package com.sparkynox.sparklauncher.di

import android.content.Context
import com.sparkynox.sparklauncher.theme.ThemeManager
import com.sparkynox.sparklauncher.utils.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * AppModule — provides PreferencesManager and ThemeManager via Hilt.
 * Author: SparkyNox
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePreferencesManager(
        @ApplicationContext context: Context
    ): PreferencesManager = PreferencesManager(context)

    @Singleton
    @Provides
    fun provideThemeManager(
        @ApplicationContext context: Context,
        prefs: PreferencesManager
    ): ThemeManager = ThemeManager(context, prefs)
}

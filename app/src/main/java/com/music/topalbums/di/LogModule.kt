package com.music.topalbums.di

import com.music.topalbums.logger.loggable.Loggable
import com.music.topalbums.logger.loggable.SimpleNameDefaultLoggable
import com.music.topalbums.logger.loggable.TestLoggable
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LogModule
{
    @Singleton
    @Binds
    abstract fun  getLoggable(loggable: SimpleNameDefaultLoggable): Loggable
    //abstract fun  getLoggable(loggable: TestLoggable): Loggable
}
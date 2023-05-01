package com.music.topalbums.di

import com.music.topalbums.utilities.IInternetConnectionChecker
import com.music.topalbums.utilities.InternetConnectionChecker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InternetConnectionModule
{
    @Binds
    abstract fun  getInternetConnectionChecker(internetConnectionChecker: InternetConnectionChecker): IInternetConnectionChecker
}

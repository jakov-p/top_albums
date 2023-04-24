package com.music.topalbums.di

import com.music.topalbums.clientapi.retrofit.ClientApi
import com.music.topalbums.clientapi.IClientApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BindApplicationModule
{
    @Binds
    abstract fun  getClientApi(clientApi: ClientApi): IClientApi
}
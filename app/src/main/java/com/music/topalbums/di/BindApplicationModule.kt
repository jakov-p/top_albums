package com.music.topalbums.di

import com.music.topalbums.clientapi.retrofit.ClientApi
import com.music.topalbums.clientapi.IClientApi
import com.music.topalbums.clientapi.retrofit.IServiceApi
import com.music.topalbums.clientapi.retrofit.ServerUrl
import com.music.topalbums.clientapi.retrofit.ServiceApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindApplicationModule
{
    @Binds
    abstract fun  getClientApi(clientApi: ClientApi): IClientApi

    @Binds
    abstract fun  getServiceApi(serviceApi: ServiceApi): IServiceApi

}
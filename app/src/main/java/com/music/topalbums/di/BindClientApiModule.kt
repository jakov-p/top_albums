package com.music.topalbums.di

import com.music.topalbums.clientapi.retrofit.ClientApi
import com.music.topalbums.clientapi.IClientApi
import com.music.topalbums.clientapi.retrofit.IServiceApi
import com.music.topalbums.clientapi.retrofit.ServiceApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BindClientApiModule
{
    @Binds
    abstract fun  getClientApi(clientApi: ClientApi): IClientApi
}


@Module
@InstallIn(SingletonComponent::class)
abstract class BindServiceApiModule
{
    @Binds
    abstract fun  getServiceApi(serviceApi: ServiceApi): IServiceApi
}
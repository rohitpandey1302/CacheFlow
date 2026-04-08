package com.rohitpandey.cache_flow.utils.di

import com.rohitpandey.cache_flow.utils.connectivity.ConnectivityObserver
import com.rohitpandey.cache_flow.utils.connectivity.ConnectivityObserverImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConnectivityModule {
    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(
        connectivityObserverImpl: ConnectivityObserverImpl,
    ): ConnectivityObserver
}
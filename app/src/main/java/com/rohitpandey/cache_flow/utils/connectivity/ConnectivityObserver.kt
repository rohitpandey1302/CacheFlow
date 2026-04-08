package com.rohitpandey.cache_flow.utils.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val networkStatus: Flow<NetworkStatus>
}
package com.rohitpandey.cache_flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohitpandey.cache_flow.utils.connectivity.ConnectivityObserver
import com.rohitpandey.cache_flow.utils.connectivity.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    connectivityObserver: ConnectivityObserver,
): ViewModel() {
    val networkStatus =
        connectivityObserver.networkStatus
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = NetworkStatus.Unavailable
            )
}
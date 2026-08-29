package com.zeta.anywhere.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zeta.anywhere.AppContainer

class MainViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(
        pairingService = container.pairingService,
        connectionService = container.connectionService,
        callService = container.callService,
        credentialStore = container.credentialStore,
        mockMode = container.config.mockMode
    ) as T
}

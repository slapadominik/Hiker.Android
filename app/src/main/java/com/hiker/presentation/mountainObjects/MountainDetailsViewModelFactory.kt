package com.hiker.presentation.mountainObjects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hiker.data.repository.MountainRemoteRepository

class MountainDetailsViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MountainDetailsViewModel(
            MountainRemoteRepository.getInstance()
        ) as T
    }

}
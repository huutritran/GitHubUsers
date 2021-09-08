package com.example.githubusers.presentation.base

import androidx.lifecycle.ViewModel
import com.example.githubusers.util.SingleLiveEvent

abstract class BaseViewModel : ViewModel() {
    val errorMessage = SingleLiveEvent<String>()
    val isLoading = SingleLiveEvent<Boolean>()


    protected open fun onFailure(throwable: Throwable) {
        //TODO handling error here
        errorMessage.value = throwable.message.orEmpty()
    }
}
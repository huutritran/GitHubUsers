package com.example.githubusers.presentation.userdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers.domain.models.UserDetail
import com.example.githubusers.domain.usecases.GetUserDetail
import com.example.githubusers.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userDetailUseCase: GetUserDetail
) : ViewModel() {
    val userDetail: LiveData<UserDetail>
        get() = _userDetail
    private val _userDetail = MutableLiveData<UserDetail>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading
    private val _isLoading = MutableLiveData<Boolean>()

    val error = SingleLiveEvent<String>()

    fun getUserDetail(userName: String) {
        if (_userDetail.value != null) {
            return
        }
        _isLoading.value = true
        userDetailUseCase(GetUserDetail.Params(userName), viewModelScope){
            _isLoading.value = false
            it
                .onFailure (this::onFailure)
                .onSuccess (this::onSuccess)
        }
    }

    private fun onSuccess(userDetail: UserDetail) {
        _userDetail.value = userDetail
    }

    private fun onFailure(throwable: Throwable) {
        error.value = throwable.message.toString()
        Log.e("onFailure", "onFailure: ", throwable)
    }
}
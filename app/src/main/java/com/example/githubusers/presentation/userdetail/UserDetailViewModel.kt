package com.example.githubusers.presentation.userdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubusers.domain.models.UserDetail
import com.example.githubusers.domain.usecases.GetUserDetail
import com.example.githubusers.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userDetailUseCase: GetUserDetail
) : BaseViewModel() {
    val userDetail: LiveData<UserDetail>
        get() = _userDetail
    private val _userDetail = MutableLiveData<UserDetail>()

    fun getUserDetail(userName: String) {
        if (_userDetail.value != null) {
            return
        }
        isLoading.value = true
        userDetailUseCase(GetUserDetail.Params(userName), viewModelScope){
            isLoading.value = false
            it
                .onFailure (this::onFailure)
                .onSuccess (this::onSuccess)
        }
    }

    private fun onSuccess(userDetail: UserDetail) {
        _userDetail.value = userDetail
    }
}
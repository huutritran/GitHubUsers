package com.example.githubusers.presentation.searchusers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers.domain.models.User
import com.example.githubusers.domain.models.UserItems
import com.example.githubusers.domain.usecases.SearchUsers
import com.example.githubusers.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchUsersViewModel @Inject constructor(
    private val searchUsers: SearchUsers
) : ViewModel() {
    val users: LiveData<List<User>>
        get() = _users
    private val _users = MutableLiveData<List<User>>()

    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    private var pageNumber = 1


    fun searchUsers(keywords: String) {
        val params = SearchUsers.Params(keywords, pageNumber)
        isLoading.value = true
        searchUsers(params, viewModelScope) {
            isLoading.value = false
            it
                .onFailure(this::onFailure)
                .onSuccess(this::onSuccess)
        }
    }

    fun hasData() : Boolean {
        return _users.value?.isNotEmpty() ?: false
    }

    private fun onSuccess(userItems: UserItems) {
        val isLastPage = checkLastPage(pageNumber, userItems.totalItems)
        if (!isLastPage) {
            pageNumber += 1
        }
        _users.value = userItems.users
    }

    private fun onFailure(throwable: Throwable) {
        errorMessage.value = throwable.message.orEmpty()
    }

    private fun checkLastPage(currentPage: Int, totalItems: Int): Boolean {
        return Constants.ITEMS_PER_PAGE * currentPage >= totalItems
    }

}
package com.example.githubusers.presentation.searchusers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubusers.domain.models.User
import com.example.githubusers.domain.models.UserItems
import com.example.githubusers.domain.usecases.SearchUsers
import com.example.githubusers.presentation.base.BaseViewModel
import com.example.githubusers.util.Constants
import com.example.githubusers.util.addAllAndNotify
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchUsersViewModel @Inject constructor(
    private val searchUsers: SearchUsers
) : BaseViewModel() {
    val users: LiveData<List<User>>
        get() = _users
    private val _users = MutableLiveData<List<User>>()

    val isLastPage = MutableLiveData<Boolean>()


    private var pageNumber = 1
    private var keywordsTemp: String = ""

    fun resetSearch() {
        keywordsTemp = ""
        pageNumber = 1
        isLastPage.value = false
        _users.value = emptyList()
    }

    fun searchUsers(keywords: String = keywordsTemp) {
        val params = SearchUsers.Params(keywords, pageNumber)
        isLoading.value = true
        keywordsTemp = keywords
        searchUsers(params, viewModelScope) {
            isLoading.value = false
            it
                .onFailure(this::onFailure)
                .onSuccess(this::onSuccess)
        }
    }

    fun getCurrentPage(): Int = pageNumber

    private fun onSuccess(userItems: UserItems) {
        if (checkLastPage(pageNumber, userItems.totalItems)) {
            this.isLastPage.value = true
        } else {
            pageNumber += 1
            this.isLastPage.value = false
        }
        _users.addAllAndNotify(userItems.users)
    }

    private fun checkLastPage(currentPage: Int, totalItems: Int): Boolean {
        return Constants.ITEMS_PER_PAGE * currentPage >= totalItems
    }

}
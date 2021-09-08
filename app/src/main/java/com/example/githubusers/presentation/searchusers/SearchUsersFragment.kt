package com.example.githubusers.presentation.searchusers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.MainActivity
import com.example.githubusers.R
import com.example.githubusers.util.addLoadMoreListener
import com.example.githubusers.util.hideKeyboard
import com.example.githubusers.util.openInBrowser
import com.example.githubusers.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_users.*


@AndroidEntryPoint
class SearchUsersFragment : Fragment() {

    private val viewModel: SearchUsersViewModel by viewModels({requireActivity()})
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupControls()
        setupRecyclerView()
        subscribeData()
    }

    private fun setupControls() {
        rvUsers.visibility = View.GONE
        imgSearchContainer.setOnClickListener {
            clearAndSearch()
        }
        etSearch.setOnEditorActionListener { _, _, _ ->
            clearAndSearch()
            return@setOnEditorActionListener true
        }
    }

    private fun subscribeData() {
        viewModel.users.observe(viewLifecycleOwner, { users ->
            val currentPage = viewModel.getCurrentPage()
            when {
                adapter.userList.isEmpty() -> {
                    adapter.updateUserList(users)
                }
                currentPage > 1 -> {
                    val startPos = adapter.userList.lastIndex + 1
                    val newItems = users.subList(startPos, users.size - 1)
                    adapter.insertUserList(newItems)
                }
            }

            if (adapter.userList.isEmpty()) {
                rvUsers.visibility = View.GONE
            } else {
                rvUsers.visibility = View.VISIBLE
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (viewModel.getCurrentPage() > 1) return@Observer
            progress_bar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.isLastPage.observe(viewLifecycleOwner, {
            adapter.isLastPage = it
        })

        viewModel.errorMessage.observe(viewLifecycleOwner,{ message ->
            toast(message)
        })
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter(this::openInBrowser, this::openUserDetail)
        val linearLayoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        val dividerItemDecoration =
            DividerItemDecoration(this.context, linearLayoutManager.orientation)

        with(rvUsers) {
            this.adapter = this@SearchUsersFragment.adapter
            layoutManager = linearLayoutManager
            addLoadMoreListener {
                if (shouldLoadMore()) {
                    viewModel.searchUsers()
                }
            }
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun shouldLoadMore(): Boolean {
        val isLoading = viewModel.isLoading.value ?: false
        return !adapter.isLastPage && !isLoading && adapter.userList.isNotEmpty()
    }

    private fun clearAndSearch() {
        adapter.clearList()
        search()
    }

    private fun search() {
        activity?.hideKeyboard()
        val keywords = etSearch.text.toString()
        if (keywords.isEmpty()) {
            return
        }
        viewModel.resetSearch()
        viewModel.searchUsers(keywords)
    }

    private fun openUserDetail(userName: String) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            (activity as MainActivity).showUserDetail(userName)
        }
    }
}
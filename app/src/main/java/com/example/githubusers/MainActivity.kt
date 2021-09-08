package com.example.githubusers

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.githubusers.presentation.searchusers.SearchUsersFragment
import com.example.githubusers.presentation.searchusers.SearchUsersViewModel
import com.example.githubusers.presentation.userdetail.UserDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showUserList()

    }

    private fun showUserList() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.container_layout,
                SearchUsersFragment(),
                SearchUsersFragment::class.java.simpleName
            ).commit()
    }

    fun showUserDetail(userName: String) {
        val detailFragment = UserDetailFragment.newInstance(userName)
        supportFragmentManager.beginTransaction()
            .addToBackStack("UserDetail")
            .replace(
                R.id.container_layout,
                detailFragment, null
            ).commit();
    }

}
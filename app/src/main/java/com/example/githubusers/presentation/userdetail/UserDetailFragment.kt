package com.example.githubusers.presentation.userdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.githubusers.R
import com.example.githubusers.util.loadCircleShape
import com.example.githubusers.util.openInBrowser
import com.example.githubusers.util.showAsLink
import com.example.githubusers.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user_detail.*
import kotlinx.android.synthetic.main.fragment_user_detail.imgAvatar


@AndroidEntryPoint
class UserDetailFragment : Fragment() {
    private val viewModel: UserDetailViewModel by viewModels()
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString(ARG_USERNAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvGitHtml.setOnClickListener {
            val html = tvGitHtml.text.toString()
            if (html.isNotEmpty()) {
                openInBrowser(html)
            }
        }
        subscribeData()
        viewModel.getUserDetail(userName.orEmpty())

    }

    private fun subscribeData() {
        viewModel.errorMessage.observe(viewLifecycleOwner, { message -> toast(message) })
        viewModel.userDetail.observe(viewLifecycleOwner, { userDetail ->

            with(userDetail) {
                imgAvatar.loadCircleShape(avatarUrl)
                tvDisplayName.text = displayName ?: name

                tvFollowing.text = following.toString()
                tvFollowers.text = followers.toString()
                tvPublicRepos.text = publicRepos.toString()

                tvBio.text = bio.orEmpty()
                tvEmail.text = email.orEmpty()
                tvLoginName.text = name
                tvGitHtml.showAsLink(html)
            }

        })

        viewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            val visibility = if (isLoading) View.VISIBLE else View.GONE
            progress_bar.visibility = visibility
        })
    }

    companion object {
        private const val ARG_USERNAME = "ARG_USERNAME"
        fun newInstance(useName: String) =
            UserDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, useName)
                }
            }
    }
}
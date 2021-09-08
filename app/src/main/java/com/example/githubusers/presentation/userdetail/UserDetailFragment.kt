package com.example.githubusers.presentation.userdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.githubusers.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserDetailFragment : Fragment() {
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

    }

    companion object {
        private const val ARG_USERNAME= "ARG_USERNAME"
        fun newInstance(useName: String) =
            UserDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, useName)
                }
            }
    }
}
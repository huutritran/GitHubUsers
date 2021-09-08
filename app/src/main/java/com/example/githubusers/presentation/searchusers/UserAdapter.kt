package com.example.githubusers.presentation.searchusers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.R
import com.example.githubusers.domain.models.User
import com.example.githubusers.util.loadCircleShape
import com.example.githubusers.util.showAsLink

class UserAdapter(
    private val onLinkClick: (html: String) -> Unit,
    private val onItemClick: (userName: String) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_USER = 1
        private const val TYPE_LOAD_MORE = 2
    }

    val userList = mutableListOf<User>()
    var isLastPage = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_USER -> UserViewHolder(inflater.inflate(R.layout.item_user, parent, false))
            else -> LoadMoreViewHolder(inflater.inflate(R.layout.item_loadmore, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserViewHolder -> {
                holder.bind(userList[position])
            }
            else -> {
            }
        }
    }

    fun clearList() {
        userList.clear()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateUserList(users: List<User>) {
        userList.clear()
        userList.addAll(users)
        notifyDataSetChanged()
    }

    fun insertUserList(users: List<User>) {
        userList.addAll(users)
        notifyItemRangeInserted(userList.size - users.size, users.size)
    }

    override fun getItemCount(): Int {
        return if (isLastPage) userList.size else userList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == userList.size) TYPE_LOAD_MORE else TYPE_USER
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgAvatar = lazy { itemView.findViewById<ImageView>(R.id.imgAvatar) }
        private val tvScore = lazy { itemView.findViewById<TextView>(R.id.tvScore) }
        private val tvUserName = lazy { itemView.findViewById<TextView>(R.id.tvUserName) }
        private val tvHtml = lazy { itemView.findViewById<TextView>(R.id.tvHtml) }

        init {
            itemView.setOnClickListener { onItemClick(userList[adapterPosition].name) }
            tvHtml.value.setOnClickListener { onLinkClick(userList[adapterPosition].html) }
        }

        fun bind(user: User) {
            with(user) {
                imgAvatar.value.loadCircleShape(avatarUrl)
                tvScore.value.text = score.toString()
                tvUserName.value.text = name
                tvHtml.value.showAsLink(user.html)
            }
        }
    }

    inner class LoadMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
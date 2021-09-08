package com.example.githubusers.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Html
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubusers.R

fun ImageView.loadCircleShape(url: String) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.ic_no_image)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}

fun RecyclerView.addLoadMoreListener(block: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            if (layoutManager?.findLastCompletelyVisibleItemPosition() ==
                recyclerView.adapter?.itemCount!! - 1) {
                block()
            }
        }
    })
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun TextView.showAsLink(url: String) {
    val formattedText = "<a href=\'$url\'>$url</a>"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text = Html.fromHtml(formattedText,  Html.FROM_HTML_MODE_LEGACY)
    } else {
        this.text = Html.fromHtml(formattedText)
    }
}

fun Fragment.toast(message: CharSequence) = context?.let {
    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
}

package com.example.githubusers.util

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<List<T>>.addAllAndNotify(items: List<T>) {
    value = when {
        value.isNullOrEmpty() -> {
            items
        }
        else -> {
            val updatedItems = value as MutableList
            updatedItems.addAll(items)
            updatedItems
        }
    }
}

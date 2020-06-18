package com.rk.appscatalog.data.models

import android.graphics.drawable.Drawable
import android.text.TextUtils
import androidx.recyclerview.widget.DiffUtil

/**
 * Created by ZMN on 17/06/2020.
 **/
data class AppItem(
    val name: String,
    val icon: Drawable?,
    val packageName: String,
    val version: String?
) {
    companion object {
        val diffItemCallback = object : DiffUtil.ItemCallback<AppItem>() {
            override fun areItemsTheSame(oldItem: AppItem, newItem: AppItem) =
                TextUtils.equals(oldItem.packageName, newItem.packageName)

            override fun areContentsTheSame(oldItem: AppItem, newItem: AppItem) = oldItem == newItem
        }
    }
}
package com.rk.appscatalog.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.rk.appscatalog.R
import com.rk.appscatalog.data.models.AppItem
import com.rk.appscatalog.utils.inflate
import com.rk.appscatalog.utils.setTextAsync

/**
 * Created by ZMN on 17/06/2020.
 **/
class AppsListRVAdapter : ListAdapter<AppItem, AppHolder>(AppItem.diffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppHolder =
        AppHolder(parent.inflate(R.layout.item_app_list))

    override fun onBindViewHolder(holder: AppHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class AppHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvName: MaterialTextView =
        itemView.findViewById(R.id.itemAppList_tvName)

    private val tvPackageName: MaterialTextView =
        itemView.findViewById(R.id.itemAppList_tvPackageName)

    private val tvVersion: MaterialTextView =
        itemView.findViewById(R.id.itemAppList_tvVersion)

    private val ivIcon: AppCompatImageView =
        itemView.findViewById(R.id.itemAppList_ivIcon)

    fun bind(appItem: AppItem) {
        tvName.setTextAsync(appItem.name)
        tvPackageName.setTextAsync(appItem.packageName)
        tvVersion.setTextAsync(appItem.version)
        appItem.icon?.let { ivIcon.setImageDrawable(it) }
            ?: ivIcon.setImageResource(R.drawable.ic_baseline_android_24)
    }

}
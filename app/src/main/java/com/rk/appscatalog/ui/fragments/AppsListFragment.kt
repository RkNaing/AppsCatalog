package com.rk.appscatalog.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rk.appscatalog.R
import com.rk.appscatalog.data.models.AppItem
import com.rk.appscatalog.ui.adapters.AppsListRVAdapter
import com.rk.appscatalog.ui.viewmodels.AppListFragmentViewModel
import com.rk.appscatalog.utils.SpacingItemDecoration
import com.rk.appscatalog.utils.makeGone
import com.rk.appscatalog.utils.makeVisible
import com.rk.appscatalog.utils.setTextAsync
import kotlinx.android.synthetic.main.fragment_apps_list.*

/**
 * Created by ZMN on 17/06/2020.
 **/

private const val TAG = "AppsListFragment"

class AppsListFragment : Fragment(R.layout.fragment_apps_list) {

    private val appsListAdapter = AppsListRVAdapter()
    private val appsListViewModel by viewModels<AppListFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appList_rv.addItemDecoration(
            SpacingItemDecoration(
                requireContext(),
                R.dimen.list_item_margin
            )
        )
        appList_rv.adapter = appsListAdapter

        appsListViewModel.appsLiveData.observe(viewLifecycleOwner, Observer { appItems ->
            updateList(appItems)
        })

        appsListViewModel.loadingLiveData.observe(
            viewLifecycleOwner,
            Observer { isLoading -> if (isLoading) showLoading() })

        appList_btgType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            Log.d(TAG, "onViewCreated: Apps List Type Changed $checkedId ($isChecked)")
            if (isChecked && checkedId != appsListViewModel.appsType) {
                appsListViewModel.appsType = checkedId
                when (checkedId) {
                    R.id.appList_btnUser -> appsListViewModel.getApps(requireContext())
                    R.id.appList_btnSystem -> appsListViewModel.getApps(requireContext(), true)
                    else -> appsListViewModel.getAllApps(requireContext())
                }
            }
        }

        appList_btgType.check(if (appsListViewModel.appsType == 0) R.id.appList_btnAll else appsListViewModel.appsType)
    }

    private fun showList() {
        appList_groupListUI.makeVisible()
        appList_groupListEmptyUI.makeGone()
        appList_pbLoading.makeGone()
    }

    private fun showLoading() {
        appList_pbLoading.makeVisible()
        appList_groupListEmptyUI.makeGone()
        appList_groupListUI.makeGone()
    }

    private fun showEmptyView() {
        appList_groupListEmptyUI.makeVisible()
        appList_pbLoading.makeGone()
        appList_groupListUI.makeGone()
        appList_btgType.makeVisible()
    }

    private fun updateList(appItems: List<AppItem>?) {
        val updatedAppItems: List<AppItem> = appItems ?: emptyList()
        appsListAdapter.submitList(updatedAppItems)
        val appsCount = updatedAppItems.size
        val lblAppsCount =
            resources.getQuantityString(R.plurals.lbl_apps_count, appsCount, appsCount)
        appList_tvCount.setTextAsync(lblAppsCount)
        if (updatedAppItems.isEmpty()) {
            showEmptyView()
        } else {
            showList()
        }
    }
}
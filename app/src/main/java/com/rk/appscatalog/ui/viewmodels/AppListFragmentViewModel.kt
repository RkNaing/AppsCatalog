package com.rk.appscatalog.ui.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.appscatalog.data.models.AppItem
import com.rk.appscatalog.data.repositories.AppsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by ZMN on 17/06/2020.
 **/
class AppListFragmentViewModel : ViewModel() {

    private val _appsLiveData: MutableLiveData<List<AppItem>> = MutableLiveData(emptyList())

    val appsLiveData: LiveData<List<AppItem>>
        get() = _appsLiveData

    private val _loadingLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveData

    var appsType: Int = 0

    fun getAllApps(context: Context) {
        loadApps { getAllApps(context) }
    }

    fun getApps(context: Context, isSystem: Boolean = false) {
        loadApps { getApps(context, isSystem) }
    }

    private inline fun loadApps(
        crossinline query: suspend AppsRepository.() -> List<AppItem>
    ) {
        viewModelScope.launch(context = Dispatchers.IO) {
            _loadingLiveData.postValue(true)
            _appsLiveData.postValue(AppsRepository.instance.query())
            _loadingLiveData.postValue(false)
        }
    }

}
package com.liverpool.test.liverpooltest.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.liverpool.test.liverpooltest.BaseApplication
import com.liverpool.test.liverpooltest.repository.database.model.Search
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel : ViewModel() {

    private val daoSearch = BaseApplication.application.database.serachDao()
    val allSearch = daoSearch.getSearch().asLiveData()

    fun deleteAll(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                daoSearch.deleteAll()
            }
        }
    }

    fun delete(search: Search){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                daoSearch.delete(search.id!!)
            }
        }
    }
}
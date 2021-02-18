package com.liverpool.test.liverpooltest.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liverpool.test.liverpool.Constants
import com.liverpool.test.liverpooltest.BaseApplication
import com.liverpool.test.liverpooltest.repository.ValidatorFactory
import com.liverpool.test.liverpooltest.repository.database.model.Search
import com.liverpool.test.liverpooltest.repository.network.Connection
import com.liverpool.test.liverpooltest.repository.network.json.response.PlpState
import com.liverpool.test.liverpooltest.repository.network.json.response.Records
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearcherViewModel : ViewModel() {
    private val repository by lazy { ValidatorFactory() }
    private val daoSearch = BaseApplication.application.database.serachDao()

    private var _allRecords : MutableLiveData<List<Records>> = MutableLiveData()
    val allRecords : LiveData<List<Records>> get() = _allRecords

    private var _pettionstatus : MutableLiveData<Int> = MutableLiveData(Constants.pettionStatus.WITHOUT_STATUS)
    val pettionstatus : LiveData<Int> get() = _pettionstatus
    private var _plpState : MutableLiveData<PlpState> = MutableLiveData()

    var page = 1
    var maxPage = 10
    private var recordsPerPage = 0


    fun getPlpStates(stringSearch: String){
        if (Connection.isConnected(BaseApplication.context)){
            viewModelScope.launch {
                try{
                    val result = withContext(Dispatchers.Main){
                        repository.getValidator(Constants.IS_RETROFIT)?.getPlpState(stringSearch)
                    }
                    if (result != null){
                        _pettionstatus.postValue(Constants.pettionStatus.SUCCESS)
                        _plpState.value = result.plpState
                        calculateRecordsPerPage()
                    }
                    else{
                        _pettionstatus.postValue(Constants.pettionStatus.ERROR_REQUEST)
                    }
                }catch (e : Throwable){
                    Log.d(Constants.LOG_TAG, "Error aqui cath")
                    _pettionstatus.postValue(Constants.pettionStatus.ERROR_REQUEST)
                }
            }
        }
        else{
            _pettionstatus.postValue(Constants.pettionStatus.WITHOUTINTERNET)
        }
    }

    fun calculateRecordsPerPage(){
        val totalRecords =  _plpState.value?.totalNumRecs
        if(totalRecords  != 0){
            recordsPerPage =  _plpState.value!!.recsPerPage
            maxPage = totalRecords!! / recordsPerPage
            getRecords()
        }
        else{
            _pettionstatus.postValue(Constants.pettionStatus.WITHOUT_RESULT)
        }
    }

    fun getRecords(){
        if (Connection.isConnected(BaseApplication.context)){
            viewModelScope.launch {
                try{
                    val result = withContext(Dispatchers.IO){
                        repository.getValidator(Constants.IS_RETROFIT)?.getProducts(_plpState.value!!.originalSearchTerm,page,recordsPerPage,)
                    }
                    if (result != null){
                        _allRecords.postValue(result)
                        _pettionstatus.postValue(Constants.pettionStatus.SUCCESS)
                    }
                    else{
                        _pettionstatus.postValue(Constants.pettionStatus.ERROR_REQUEST)
                    }
                }catch (e : Throwable){
                    _pettionstatus.postValue(Constants.pettionStatus.ERROR_REQUEST)
                }
            }
        }
        else{
            _pettionstatus.postValue(Constants.pettionStatus.WITHOUTINTERNET)
        }
    }

    fun resetPetitionStatus(){
        _pettionstatus.postValue(Constants.pettionStatus.WITHOUT_STATUS)
    }

    fun saveSearch(stringSearch: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                daoSearch.insert(Search(null,stringSearch))
            }
        }
    }

    fun existBack() = page > 1

    fun existNext() = page < maxPage

    fun onBackPage(){
        page -= 1
        getRecords()
    }

    fun onNextPage(){
        page += 1
        getRecords()
    }
}
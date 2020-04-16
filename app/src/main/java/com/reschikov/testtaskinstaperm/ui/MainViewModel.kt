package com.reschikov.testtaskinstaperm.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reschikov.testtaskinstaperm.MILLI_SEC
import com.reschikov.testtaskinstaperm.model.Authorization
import com.reschikov.testtaskinstaperm.model.Signal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch



class MainViewModel(private var derivable: Derivable?) : ViewModel() {

    @ExperimentalCoroutinesApi
    private val errorChannel = Channel<Throwable>()
    private val isAuthorizedLiveData = MutableLiveData<Boolean>()
    private val hasProgressLiveData = MutableLiveData<Boolean>()
    private val fromLiveData = MutableLiveData<Long>(0L)
    private val toLiveData = MutableLiveData<Long>(0L)
    private val signalsLiveData = MutableLiveData<List<Signal>>()

    init {
        isAuthorizedLiveData.value = false
        hasProgressLiveData.value = false
    }

    @ExperimentalCoroutinesApi
    fun getErrorChannel() = errorChannel
    fun isAuthorizedLiveData() = isAuthorizedLiveData
    fun hasVisibleProgressLiveData() = hasProgressLiveData
    fun getFromDateLiveData() = fromLiveData
    fun getToDateLiveData() = toLiveData
    fun getSignalsLiveData() = signalsLiveData

    fun setFromDate(from : Long){
        this.fromLiveData.value = from
    }

    fun setToDate (to : Long){
        this.toLiveData.value = to
    }

    @ExperimentalCoroutinesApi
    fun toReportMessage(string: String) {
        viewModelScope.launch {
            errorChannel.send(Throwable(string))
        }
    }

    @ExperimentalCoroutinesApi
    fun logIn (authorization: Authorization){
        hasProgressLiveData.value = true
        viewModelScope.launch(Dispatchers.IO){
            derivable?.hasAuthorization(authorization)?.let {
                isAuthorizedLiveData.postValue(it.first)
                it.second?.let {err ->
                    isAuthorizedLiveData.postValue(false)
                    errorChannel.send(err)
                }
                hasProgressLiveData.postValue(false)
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun getListSignalsInstrument(pairs: Array<String>){
        val from = fromLiveData.value
        val to = toLiveData.value
        if (from == null || to == null) return
        hasProgressLiveData.value = true
        viewModelScope.launch(Dispatchers.IO){
            derivable?.getListSignals(pairs, createUnixDate(from), createUnixDate(to))?.let {
                it.first?.let {list -> signalsLiveData.postValue(list) }
                it.second?.let {err ->
                    errorChannel.send(err)
                    hasProgressLiveData.postValue(false)
                }
            }
        }
    }

    private fun createUnixDate(long : Long) : Long = long / MILLI_SEC

    override fun onCleared() {
        super.onCleared()
        derivable = null
    }
}
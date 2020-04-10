package com.reschikov.testtaskinstaperm.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reschikov.testtaskinstaperm.model.Authorization
import com.reschikov.testtaskinstaperm.model.Signal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch

class MainViewModel(private var derivable: Derivable?) : ViewModel() {

    @ExperimentalCoroutinesApi
    private val errorChannel : BroadcastChannel<Throwable?> = BroadcastChannel(Channel.CONFLATED)
    @ExperimentalCoroutinesApi
    private val successChannel : BroadcastChannel<Boolean> = BroadcastChannel(Channel.CONFLATED)

    @ExperimentalCoroutinesApi
    fun getErrorChannel() = errorChannel.openSubscription()
    @ExperimentalCoroutinesApi
    fun getSuccessChannel() = successChannel.openSubscription()

    @ExperimentalCoroutinesApi
    fun logIn (authorization: Authorization){
        viewModelScope.launch(Dispatchers.IO){
            derivable?.hasAuthorization(authorization)?.let {
                successChannel.send(it.first)
                it.second?.let {err -> errorChannel.send(err) }
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun getListSignalsInstrument(pairs: String, from: Long, to: Long) : ReceiveChannel<List<Signal>>{
        return viewModelScope.produce(Dispatchers.IO){
            derivable?.getListSignals(pairs, from, to)?.let {
               it.first?.let {list -> send(list) }
               it.second?.let {err -> errorChannel.send(err) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        derivable = null
    }
}
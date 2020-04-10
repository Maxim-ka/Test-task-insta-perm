package com.reschikov.testtaskinstaperm.ui.showdata

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.reschikov.testtaskinstaperm.R
import com.reschikov.testtaskinstaperm.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_list_signals.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.StringBuilder
import kotlin.coroutines.CoroutineContext

private const val FROM = 1479860023L
private const val TO = 1480066860L

class ListSignalsFragment : Fragment(R.layout.fragment_list_signals), CoroutineScope {

    override val coroutineContext : CoroutineContext by lazy {
        Dispatchers.Main + SupervisorJob()
    }

    private val viewModel: MainViewModel by sharedViewModel()
    private val adapterListSignals : AdapterListSignals = AdapterListSignals()
    private lateinit var successJob : Job


    private fun createPairs() : String {
        val arr = resources.getStringArray(R.array.instruments)
        val sb = StringBuilder()
        for (str in arr){
            sb.append(str)
            if (arr.indexOf(str) < arr.size - 1) sb.append(",")
        }
        return sb.toString()
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_list.adapter = adapterListSignals
        rv_list.setHasFixedSize(true)
        successJob = launch {
            adapterListSignals.list = viewModel.getListSignalsInstrument(createPairs(), FROM, TO).receive()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        successJob.cancel()
        rv_list.adapter = null
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }
}
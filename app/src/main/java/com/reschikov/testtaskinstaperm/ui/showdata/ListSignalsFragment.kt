package com.reschikov.testtaskinstaperm.ui.showdata

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.reschikov.testtaskinstaperm.R
import com.reschikov.testtaskinstaperm.model.Signal
import com.reschikov.testtaskinstaperm.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_list_signals.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ListSignalsFragment : Fragment(R.layout.fragment_list_signals){

    private val viewModel: MainViewModel by sharedViewModel()
    private val adapterListSignals : AdapterListSignals = AdapterListSignals()
    @ExperimentalCoroutinesApi
    private val observerListSignals by lazy {
        Observer<List<Signal>> {
            if (it.isNotEmpty()) adapterListSignals.list = it
            else viewModel.toReportMessage(getString(R.string.err_no_data_on_request))
            viewModel.hasVisibleProgressLiveData().value = false
        }
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_list.adapter = adapterListSignals
        rv_list.setHasFixedSize(true)
        viewModel.getSignalsLiveData().observe(this, observerListSignals)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rv_list.adapter = null
    }
}
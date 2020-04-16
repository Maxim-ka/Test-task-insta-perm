package com.reschikov.testtaskinstaperm.ui.parameters

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.reschikov.testtaskinstaperm.KEY_TAG
import com.reschikov.testtaskinstaperm.R
import com.reschikov.testtaskinstaperm.ui.MainViewModel
import com.reschikov.testtaskinstaperm.ui.dialogs.showAlertDialog
import kotlinx.android.synthetic.main.block_date_picker.*
import kotlinx.android.synthetic.main.block_tool_selection_buttons.*
import kotlinx.android.synthetic.main.fragment_parameter_selection.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val KEY_TOOLS = "key tools"

class ToolSelectionFragment : Fragment(R.layout.fragment_parameter_selection) {

    private val viewModel : MainViewModel by  sharedViewModel()
    private val navController : NavController by lazy { findNavController() }
    private val listenerInstruments : View.OnClickListener by lazy {
        View.OnClickListener { it.isActivated = !it.isActivated }
    }
    private val listenerSelectFromDate : View.OnClickListener by lazy {
        View.OnClickListener{
            loadSelectDateDialog(getString(R.string.title_from_date))
        }
    }
    private val listenerSelectToDate : View.OnClickListener by lazy {
        View.OnClickListener{
            loadSelectDateDialog(getString(R.string.title_to_date))
        }
    }
    @ExperimentalCoroutinesApi
    private val listenerButSend : View.OnClickListener by lazy {
        View.OnClickListener{
            if (checkParameters()) {
                listTools?.let { viewModel.getListSignalsInstrument(it.toTypedArray()) }
                navController.navigate(R.id.action_toolSelectionFragment_to_listSignalsFragment)
            }
        }
    }
    private val observerChooseFromDate by lazy {
        Observer<Long> {
            if (it == 0L) return@Observer
            val toDay = getToDate()
            if (toDay != null && it >= toDay && toDay != 0L){
                til_date_begin.error = getString(R.string.err_start_date_can_NOT)
            } else {
                tiet_date_begin.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(Date(it)))
                til_date_begin.error = null
            }
        }
    }
    private val observerChooseToDate by lazy {
        Observer<Long> {
            if (it == 0L) return@Observer
            val fromDay = getFromDate()
            if (fromDay != null && it <= fromDay && fromDay != 0L){
                til_date_end.error = getString(R.string.err_end_date_can_NOT)
            } else {
                til_date_end.error = null
                tiet_date_end.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(Date(it)))
            }
        }
    }
    private val observerVisibilityProcess by lazy {
        Observer<Boolean> { but_send.isEnabled = !it }
    }

    private var listTools : List<String>? = null
    private lateinit var buttons : List<Button>

    private fun getFromDate() : Long? = viewModel.getFromDateLiveData().value
    private fun getToDate() : Long? = viewModel.getToDateLiveData().value

    private fun loadSelectDateDialog(tag : String){
        navController.navigate(R.id.action_toolSelectionFragment_to_dateSettingDialog, createBundle(tag))
    }

    private fun createBundle(str: String) : Bundle = Bundle().apply { putString(KEY_TAG, str) }

    private fun checkParameters() : Boolean{
        val from = getFromDate()
        val to = getToDate()
        if (from == null || from == 0L){
            til_date_begin.error = getString(R.string.err_field_not_filled)
            return false
        }
        if (to == null  || to == 0L){
            til_date_end.error = getString(R.string.err_field_not_filled)
            return false
        }
        if (from >= to) return false
        return hasTools()
    }

    private fun hasTools() : Boolean{
        listTools = createListSelectTools()
        listTools?.let {
            if (it.isEmpty()){
                showAlertDialog()
                return false
            }
        }
        return true
    }

    private fun createListSelectTools() : List<String>{
        val list = mutableListOf<String>()
        for(button in buttons){
            if (button.isActivated) list.add(button.text.toString())
        }
        return list
    }

    private fun showAlertDialog(){
        activity?.showAlertDialog(getString(R.string.attention), getString(R.string.err_correct_choice))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons = listOf<Button>(but_EURUSD, but_GBPUSD, but_USDJPY, but_AUDUSD,
            but_NZDUSD, but_USDCAD, but_USDCHF)
        savedInstanceState?.let {
            it.getStringArrayList(KEY_TOOLS)?.let {list -> setActivatedTools(list)}
        }
        viewModel.getFromDateLiveData().observe(this, observerChooseFromDate)
        viewModel.getToDateLiveData().observe(this, observerChooseToDate)
        viewModel.hasVisibleProgressLiveData().observe(this, observerVisibilityProcess)
    }

    private fun setActivatedTools(list: List<String>){
        lab@ for (button in buttons){
            for (str in list){
                if (button.text == str){
                    button.isActivated = true
                    continue@lab
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        setListenerInstruments(listenerInstruments)
        tiet_date_begin.setOnClickListener(listenerSelectFromDate)
        tiet_date_end.setOnClickListener(listenerSelectToDate)
        but_send.setOnClickListener(listenerButSend)
    }

    override fun onResume() {
        super.onResume()
        validationOfActiveTools()
    }

    private fun validationOfActiveTools(){
        listTools?.let {
            if (it.isEmpty()) return
            setActivatedTools(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val list = ArrayList<String>(createListSelectTools())
        if (list.isNotEmpty()){
            outState.putStringArrayList(KEY_TOOLS, list)
        }
    }

    override fun onStop() {
        super.onStop()
        setListenerInstruments(null)
        tiet_date_begin.setOnClickListener(null)
        tiet_date_end.setOnClickListener(null)
        but_send.setOnClickListener(null)
    }

    private fun setListenerInstruments(listener: View.OnClickListener?){
        for (button in buttons){
            button.setOnClickListener(listener)
        }
    }

}
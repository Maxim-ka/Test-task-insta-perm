package com.reschikov.testtaskinstaperm.ui.dialogs

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.reschikov.testtaskinstaperm.KEY_TAG
import com.reschikov.testtaskinstaperm.R
import com.reschikov.testtaskinstaperm.ui.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class DateSettingDialog : DialogFragment(), OnDateSetListener {

    private val viewModel : MainViewModel by sharedViewModel()
    private lateinit var title : String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.let {activity ->
            arguments?.let {arg -> arg.getString(KEY_TAG)?.let { title = it }}
            return Calendar.getInstance().let {
                DatePickerDialog(
                    activity,
                    this,
                    it[Calendar.YEAR],
                    it[Calendar.MONTH],
                    it[Calendar.DAY_OF_MONTH]
                )
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.setTitle(title)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        Calendar.getInstance().run {
            set(year, month, dayOfMonth)
            when(title){
                getString(R.string.title_from_date) -> viewModel.setFromDate(timeInMillis)
                else -> viewModel.setToDate(timeInMillis)
            }
        }
    }
}
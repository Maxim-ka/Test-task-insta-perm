package com.reschikov.testtaskinstaperm.ui.authorization

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.reschikov.testtaskinstaperm.R
import com.reschikov.testtaskinstaperm.model.Authorization
import com.reschikov.testtaskinstaperm.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_authorization.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AuthorizationFragment : DialogFragment() {

    private val viewModel : MainViewModel by sharedViewModel()
    private val observerVisibilityProcess by lazy {
        Observer<Boolean> {
            tiet_login.isEnabled = !it
            tiet_password.isEnabled = !it
            but_logIn.isEnabled = !it
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val  view = inflater.inflate(R.layout.fragment_authorization, container, false)
        viewModel.hasVisibleProgressLiveData().observe(this, observerVisibilityProcess)
        return view
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        but_logIn.setOnClickListener { v: View ->
            createAuthorization()?.let {
                viewModel.logIn(it)
            }
        }
    }

    private fun createAuthorization() : Authorization?{
        val login = tiet_login.text.toString()
        val pass = tiet_password.text.toString()
        if (login.isEmpty() && login.isBlank()){
            til_login.error = getString(R.string.err_field_not_filled)
            return null
        }
        if (pass.isEmpty() && pass.isBlank()){
            til_password.error = getString(R.string.err_field_not_filled)
            return null
        }
        til_login.error = null
        til_password.error = null
        return Authorization(login, pass)
    }

    override fun onStop() {
        super.onStop()
        but_logIn.setOnClickListener(null)
    }
}
package com.reschikov.testtaskinstaperm.ui.authorization

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.reschikov.testtaskinstaperm.R
import com.reschikov.testtaskinstaperm.model.Authorization
import com.reschikov.testtaskinstaperm.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_authorization.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AuthorizationFragment : DialogFragment() {

    private val viewModel : MainViewModel by sharedViewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_authorization, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        button.setOnClickListener { v: View ->
            createAuthorization()?.let {
                viewModel.logIn(it)
            }
        }
    }

    private fun createAuthorization() : Authorization?{
        val login = tiet_login.text.toString()
        val pass = tiet_password.text.toString()
        if (login.isNotEmpty() && login.isNotBlank() && pass.isNotEmpty() && pass.isNotBlank()){
            return Authorization(login, pass)
        }
        return null
    }

    override fun onStop() {
        super.onStop()
        button.setOnClickListener(null)
    }
}
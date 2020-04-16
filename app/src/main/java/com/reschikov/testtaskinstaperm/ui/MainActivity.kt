package com.reschikov.testtaskinstaperm.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.reschikov.testtaskinstaperm.R
import com.reschikov.testtaskinstaperm.ui.dialogs.showAlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext : CoroutineContext by lazy {
        Dispatchers.Main + SupervisorJob()
    }
    private val navController: NavController by lazy { Navigation.findNavController(this, R.id.frame_master) }

    private val observerIsAuthorized  by lazy {
        Observer<Boolean> {
            if (it) {
                if (navController.currentDestination?.id == R.id.authorizationFragment){
                    navController.popBackStack()
                }
            } else {
                if (navController.currentDestination?.id == R.id.toolSelectionFragment){
                    navController.navigate(R.id.action_toolSelectionFragment_to_authorizationFragment)
                }
            }
        }
    }

    private val observerVisibilityProcess by lazy {
        Observer<Boolean> {
            pb_circle.visibility = if(it) View.VISIBLE else View.GONE
        }
    }

    private val viewModel : MainViewModel by viewModel()
    private lateinit var errorJob : Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.isAuthorizedLiveData().observe(this, observerIsAuthorized)
        viewModel.hasVisibleProgressLiveData().observe(this, observerVisibilityProcess)
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        errorJob = launch{
            renderError(viewModel.getErrorChannel().receive())
        }
    }

    private fun renderError(e : Throwable){
        showAlertDialog(getString(R.string.dialog_title_warning), "" + e.message)
        navController.popBackStack(R.id.toolSelectionFragment, false)
    }

    override fun onStop () {
        super .onStop()
        errorJob.cancel()
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.authorizationFragment) {
            finish()
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }
}

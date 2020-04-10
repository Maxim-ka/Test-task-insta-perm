package com.reschikov.testtaskinstaperm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.reschikov.testtaskinstaperm.R
import com.reschikov.testtaskinstaperm.ui.authorization.AuthorizationFragment
import com.reschikov.testtaskinstaperm.ui.showdata.ListSignalsFragment
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

private const val TAG_AUTHORIZATION = "tag Authorization"
private const val TAG_LIST_SIGNALS = "tag ListSignals"

class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext : CoroutineContext by lazy {
        Dispatchers.Main + SupervisorJob()
    }

    private val viewModel : MainViewModel by viewModel()
    private lateinit var errorJob : Job
    private lateinit var successJob : Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_master, AuthorizationFragment(), TAG_AUTHORIZATION)
                .commit()
        }
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        errorJob = launch{
            viewModel.getErrorChannel().consumeEach { e ->
                e?.let { renderError(it) }
            }
        }
        successJob = launch {
            if (viewModel.getSuccessChannel().receive()){
                loadFragment(ListSignalsFragment(), TAG_LIST_SIGNALS)
            }
        }
    }

    private fun loadFragment(fragment: Fragment, tag : String){
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_master, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    private fun renderError(e : Throwable){
        Toast.makeText(baseContext, e.message, Toast.LENGTH_LONG).show()
    }

    override fun onStop () {
        super .onStop()
        successJob.cancel()
        errorJob.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }
}

package com.reschikov.testtaskinstaperm.data.cache

import android.util.Log
import com.reschikov.testtaskinstaperm.data.repository.Cached
import com.reschikov.testtaskinstaperm.model.Signal
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CacheProvider(private val storable: Storable) : Cached, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()

    override fun toCache(signals: List<Signal>) {
        launch {
            try {
                storable.save(signals)
            } catch (e : Throwable){
                Log.i("TAG CacheProvider", "toCache: " + e.message)
            }
        }
    }

    override fun takeFromCache(pairs: Array<String>, from: Long, to: Long): List<Signal> {
        return try {
            storable.getSignals(pairs, from, to)
        } catch (e : Exception) {
            Log.i("TAG CacheProvider", "takeFromCache: " + e.message)
            listOf()
        }
    }
}
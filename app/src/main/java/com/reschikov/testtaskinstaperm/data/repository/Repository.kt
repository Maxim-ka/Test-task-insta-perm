package com.reschikov.testtaskinstaperm.data.repository

import android.util.Log
import com.reschikov.testtaskinstaperm.model.Authorization
import com.reschikov.testtaskinstaperm.model.Signal
import com.reschikov.testtaskinstaperm.ui.Derivable
import java.lang.StringBuilder

class Repository (private val requested: Requested,
                  private val cached: Cached,
                  private val transformed: Transformed) : Derivable {

    override suspend fun hasAuthorization(authorization: Authorization): Pair<Boolean, Throwable?> {
        return try {
            Pair(requested.logIn(authorization), null)
        } catch (e : Throwable){
            Pair(false, e)
        }
    }

    override suspend fun getListSignals(pairs: Array<String>, from: Long, to: Long): Pair<List<Signal>?, Throwable?> {
        return try {
            var signals = getFromCache(pairs, from, to)
            Log.i("TAG getListSignals", signals.toString())
            if (signals.isEmpty() || signals.size != pairs.size){
                signals = requestServer(pairs, from, to)
                saveToCache(signals)
            }
            Pair(signals, null)
        } catch (e : Throwable){
            Pair(null, e)
        }
    }

    private fun getFromCache(pairs: Array<String>, from: Long, to: Long) : List<Signal>{
        return cached.takeFromCache(pairs, from, to)
    }

    private suspend fun requestServer(pairs: Array<String>, from: Long, to: Long) : List<Signal>{
        return transformed.transform(requested.getListOfSignals(createPairs(pairs), from, to))
    }

    private fun createPairs(pairs: Array<String>) : String {
        val sb = StringBuilder()
        for (str in pairs){
            sb.append(str)
            if (pairs.indexOf(str) < pairs.size - 1) sb.append(",")
        }
        return sb.toString()
    }

    private fun saveToCache(signals: List<Signal>){
        cached.toCache(signals)
    }
}
package com.reschikov.testtaskinstaperm.data.repository

import com.reschikov.testtaskinstaperm.model.Authorization
import com.reschikov.testtaskinstaperm.model.Signal
import com.reschikov.testtaskinstaperm.ui.Derivable

class Repository (private val requested: Requested) : Derivable {

    override suspend fun hasAuthorization(authorization: Authorization): Pair<Boolean, Throwable?> {
        return try {
            Pair(requested.logIn(authorization), null)
        } catch (e : Throwable){
            Pair(false, e)
        }
    }

    override suspend fun getListSignals(pairs: String, from: Long, to: Long): Pair<List<Signal>?, Throwable?> {
        return try {
            Pair(requested.getListOfSignals(pairs,from, to), null)
        } catch (e : Throwable){
            Pair(null, e)
        }
    }
}
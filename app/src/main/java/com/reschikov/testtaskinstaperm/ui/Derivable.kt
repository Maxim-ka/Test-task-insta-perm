package com.reschikov.testtaskinstaperm.ui

import com.reschikov.testtaskinstaperm.model.Authorization
import com.reschikov.testtaskinstaperm.model.Signal

interface Derivable {

    suspend fun hasAuthorization(authorization: Authorization) : Pair<Boolean, Throwable?>
    suspend fun getListSignals(pairs: Array<String>, from: Long, to: Long) : Pair<List<Signal>?, Throwable?>
}
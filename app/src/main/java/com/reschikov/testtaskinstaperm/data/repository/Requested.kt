package com.reschikov.testtaskinstaperm.data.repository

import com.reschikov.testtaskinstaperm.model.Authorization
import com.reschikov.testtaskinstaperm.model.Signal

interface Requested {

    suspend fun logIn(authorization: Authorization) : Boolean
    suspend fun getListOfSignals(pairs: String, from: Long, to: Long): List<Signal>
}
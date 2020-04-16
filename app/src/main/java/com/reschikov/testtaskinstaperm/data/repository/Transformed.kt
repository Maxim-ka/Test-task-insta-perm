package com.reschikov.testtaskinstaperm.data.repository

import com.reschikov.testtaskinstaperm.data.network.model.ServerResponse
import com.reschikov.testtaskinstaperm.model.Signal

interface Transformed {
    fun transform(responses: List<ServerResponse>) : List<Signal>
}
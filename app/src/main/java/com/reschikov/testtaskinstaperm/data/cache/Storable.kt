package com.reschikov.testtaskinstaperm.data.cache

import com.reschikov.testtaskinstaperm.model.Signal

interface Storable {

    fun save(signals: List<Signal>)
    fun getSignals(pairs: Array<String>, from: Long, to: Long): List<Signal>
}
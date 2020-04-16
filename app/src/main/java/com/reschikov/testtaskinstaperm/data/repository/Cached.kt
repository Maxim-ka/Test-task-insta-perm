package com.reschikov.testtaskinstaperm.data.repository

import com.reschikov.testtaskinstaperm.model.Signal

interface Cached {

    fun toCache(signals : List<Signal>)
    fun takeFromCache(pairs : Array<String>, from : Long, to : Long) : List<Signal>
}
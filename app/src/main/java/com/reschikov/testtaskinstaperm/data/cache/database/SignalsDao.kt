package com.reschikov.testtaskinstaperm.data.cache.database

import androidx.room.*
import com.reschikov.testtaskinstaperm.data.cache.Storable
import com.reschikov.testtaskinstaperm.model.Signal

@Dao
abstract class SignalsDao : Storable {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun save(signals: List<Signal>)

    @Query("SELECT * FROM signal WHERE pair= :namePair AND actualTime BETWEEN :from AND :to")
    abstract fun getSignal(namePair: String, from: Long, to: Long) : Signal?

    @Transaction
    override fun getSignals(pairs: Array<String>, from: Long, to: Long): List<Signal>{
        val list = mutableListOf<Signal>()
        for (pair in pairs){
            getSignal(pair, from, to)?.let { list.add(it) }
        }
        return list
    }
}
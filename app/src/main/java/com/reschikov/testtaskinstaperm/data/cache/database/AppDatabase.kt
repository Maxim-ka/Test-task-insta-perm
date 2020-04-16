package com.reschikov.testtaskinstaperm.data.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reschikov.testtaskinstaperm.model.Signal

@Database(entities = [Signal::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun signalsDao() : SignalsDao
}
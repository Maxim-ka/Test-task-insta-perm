package com.reschikov.testtaskinstaperm.model

import androidx.room.Entity

@Entity(primaryKeys = ["id"])
data class Signal(val id: Int,
                  val actualTime: Long,
                  val pair: String,
                  val price: Float,
                  val sl: Float,
                  val tp: Float)
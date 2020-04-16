package com.reschikov.testtaskinstaperm.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ServerResponse(@SerializedName("Id") @Expose val id: Int,
                          @SerializedName("ActualTime") @Expose val actualTime: Long,
                          @SerializedName("Comment") @Expose val comment: String,
                          @SerializedName("Pair") @Expose val pair: String,
                          @SerializedName("Cmd") @Expose val cmd: Int,
                          @SerializedName("TradingSystem") @Expose val tradingSystem: Int,
                          @SerializedName("Period") @Expose val period: String,
                          @SerializedName("Price") @Expose val price: Float,
                          @SerializedName("Sl") @Expose val sl: Float,
                          @SerializedName("Tp") @Expose val tp: Float)
package com.reschikov.testtaskinstaperm.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Authorization(@SerializedName("Login") @Expose val login: String,
                         @SerializedName("Password") @Expose val password: String)
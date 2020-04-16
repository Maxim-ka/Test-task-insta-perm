package com.reschikov.testtaskinstaperm.data.network

import com.reschikov.testtaskinstaperm.model.Authorization
import com.reschikov.testtaskinstaperm.data.network.model.ServerResponse
import retrofit2.Call
import retrofit2.http.*

interface ObtainedFromInstaforex {

    @POST("api/Authentication/RequestMoblieCabinetApiToken")
    fun logIn (@Body authorization: Authorization) : Call<String>

    @GET("clientmobile/GetAnalyticSignals/{login}")
    fun getListSignals(@Header ("passkey") token : String,
                       @Path("login") login : String,
                       @Query ("tradingsystem") tradingsystem : Int,
                       @Query ("pairs") pairs : String,
                       @Query ("from") from : Long,
                       @Query ("to") to : Long) : Call<List<ServerResponse>>
}
package com.reschikov.testtaskinstaperm.data.network

import com.reschikov.testtaskinstaperm.data.repository.Requested
import com.reschikov.testtaskinstaperm.model.Authorization
import com.reschikov.testtaskinstaperm.model.Signal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val URL_SERVER = "http://client-api.instaforex.com/"
private const val TRADING_SYSTEM = 3

class ServerRequest : Requested{

    private val request = Retrofit.Builder()
        .baseUrl(URL_SERVER)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ObtainedFromInstaforex::class.java)

    private lateinit var login : String
    private lateinit var passkey : String

    override suspend fun logIn(authorization: Authorization): Boolean {
        return suspendCoroutine{continuation ->
            request.logIn(authorization)
                .enqueue(object : Callback<String>{
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(response.isSuccessful){
                            response.body()?.let {
                                this@ServerRequest.login = authorization.login
                                passkey = it
                                continuation.resume(true)
                            }
                        }
                        response.errorBody()?.let { continuation.resumeWithException(Throwable(it.string())) }
                    }
                })
        }
    }

    override suspend fun getListOfSignals(pairs: String, from: Long, to: Long): List<Signal> {
        return suspendCoroutine{continuation ->
            request.getListSignals(passkey, login, TRADING_SYSTEM, pairs, from, to)
                .enqueue(object : Callback<List<Signal>>{
                    override fun onFailure(call: Call<List<Signal>>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }

                    override fun onResponse(call: Call<List<Signal>>, response: Response<List<Signal>>) {
                        if(response.isSuccessful){
                            response.body()?.let {
                                continuation.resume(it)
                            }
                        }
                        response.errorBody()?.let { continuation.resumeWithException(Throwable(it.string())) }
                    }
                })
        }
    }
}
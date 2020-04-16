package com.reschikov.testtaskinstaperm.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import com.reschikov.testtaskinstaperm.R
import com.reschikov.testtaskinstaperm.data.repository.Requested
import com.reschikov.testtaskinstaperm.model.Authorization
import com.reschikov.testtaskinstaperm.data.network.model.ServerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val URL_SERVER = "https://client-api.instaforex.com/"
private const val TRADING_SYSTEM = 3

class ServerRequest(private  val context: Context) : Requested{

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val strNoNetWork by lazy { context.getString(R.string.dialog_no_network) }
    private val strNoAuthorization by lazy { context.getString(R.string.dialog_no_authorization) }
    private val request = Retrofit.Builder()
        .baseUrl(URL_SERVER)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ObtainedFromInstaforex::class.java)

    private var login : String? = null
    private var passkey : String? = null

    @Throws()
    override suspend fun logIn(authorization: Authorization): Boolean {
        if (isNoNetwork()) throw Throwable(strNoNetWork)
        if (login != null && login == authorization.login && passkey != null) return true
        return suspendCoroutine{continuation ->
            request.logIn(authorization)
                .enqueue(object : Callback<String>{
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        continuation.resumeWithException(Throwable(strNoAuthorization + t.message))
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(response.isSuccessful){
                            response.body()?.let {
                                this@ServerRequest.login = authorization.login
                                passkey = it
                                continuation.resume(true)
                                return
                            }
                        }
                        this@ServerRequest.login = null
                        passkey = null
                        continuation.resumeWithException(Throwable(strNoAuthorization + response.errorBody()?.string()))
                    }
                })
        }
    }

    @Throws()
    override suspend fun getListOfSignals(pairs: String, from: Long, to: Long): List<ServerResponse> {
        if (isNoNetwork()) throw Throwable(strNoNetWork)
        passkey?.let { passkey ->
            login?.let { login ->
                return suspendCoroutine{continuation ->
                    request.getListSignals(passkey, login, TRADING_SYSTEM, pairs, from, to)
                        .enqueue(object : Callback<List<ServerResponse>>{
                            override fun onFailure(call: Call<List<ServerResponse>>, t: Throwable) {
                                continuation.resumeWithException(t)
                            }

                            override fun onResponse(call: Call<List<ServerResponse>>, response: Response<List<ServerResponse>>) {
                                if(response.isSuccessful){
                                    response.body()?.let {
                                        continuation.resume(it)
                                        return
                                    }
                                }
                                continuation.resumeWithException(Throwable(response.errorBody()?.string()))
                            }
                        })
                }
            }
        }
        throw Throwable(strNoAuthorization)
    }

    private fun isNoNetwork() : Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return connectivityManager.activeNetwork == null
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            return !connectivityManager.isDefaultNetworkActive
        }
        val networkInfo = connectivityManager .activeNetworkInfo
        return networkInfo == null || !networkInfo.isConnectedOrConnecting
    }
}
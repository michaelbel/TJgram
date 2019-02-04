package org.michaelbel.tjgram.utils

import android.content.Context
import android.net.ConnectivityManager
import retrofit2.HttpException

object NetworkUtil {

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    @Suppress("unused")
    fun getHttpCode(throwable: Throwable): Int {
        return (throwable as HttpException).code()
    }

    fun isHttpStatusCode(throwable: Throwable, statusCode: Int): Boolean {
        return throwable is HttpException && throwable.code() == statusCode
    }
}
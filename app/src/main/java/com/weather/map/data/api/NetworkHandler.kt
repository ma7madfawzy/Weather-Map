package com.weather.map.data.api

import android.util.Log
import com.weather.map.domain.model.KeyValueDM
import com.weather.map.domain.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.net.HttpURLConnection
import java.net.URL

object NetworkHandler {
    private val TAG = NetworkHandler::class.qualifiedName

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun request(url: URL, method:String,vararg headers: KeyValueDM): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val reader: BufferedReader

                with(url.openConnection() as HttpURLConnection) {
                    Log.i("$TAG opening connection", url.toString())
                    requestMethod = method
                    setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ")
                    headers.forEach { setRequestProperty(it.key, it.value) }
                    doInput = true
                    reader = BufferedReader(InputStreamReader(inputStream) as Reader?)

                    val response = StringBuffer()
                    var inputLine = reader.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = reader.readLine()
                    }
                    reader.close()
                    Log.i("$TAG connection response", response.toString())
                    Result.Success(response.toString())
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(e.message.toString())
            }
        }
    }

}
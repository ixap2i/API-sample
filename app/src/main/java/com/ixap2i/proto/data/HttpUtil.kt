package com.ixap2i.proto.data

import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request

class HttpUtil {

    fun getCookingRecords(url: String,  client: OkHttpClient): Deferred<String?>
            = GlobalScope.async(Dispatchers.Default, CoroutineStart.DEFAULT, {
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        return@async response.body()?.string()
    })


}
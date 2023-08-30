package com.myket.farahani.dynamictoken.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface FileDownloadApi {
    @Streaming
    @GET
    suspend fun downloadFile(@Url url:String): ResponseBody
}

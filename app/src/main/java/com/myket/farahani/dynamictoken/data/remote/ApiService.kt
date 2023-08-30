package com.myket.farahani.dynamictoken.data.remote

import com.myket.farahani.dynamictoken.data.remote.model.AppResponse
import com.myket.farahani.dynamictoken.data.remote.model.TokenResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    @GET(ApiConstants.CALCULATION_DATA)
    suspend fun getCalcData() : List<Int>

    @GET(ApiConstants.TOKEN)
    suspend fun getToken(
        @Path("answer") answer: String
    ): String

    @GET(ApiConstants.APP_DATA)
    suspend fun getAppData(@Header("Authorization") token:String):AppResponse
}
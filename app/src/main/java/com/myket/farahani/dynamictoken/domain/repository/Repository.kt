package com.myket.farahani.dynamictoken.domain.repository

import com.myket.farahani.dynamictoken.data.remote.model.AppResponse
import okhttp3.ResponseBody

interface Repository {
    suspend fun getCalcData(): Result<List<Int>>

    suspend fun getToken(answer: String): Result<String>

    suspend fun getAppData(token: String): Result<AppResponse>

    suspend fun downloadFile(url: String): Result<ResponseBody>
}
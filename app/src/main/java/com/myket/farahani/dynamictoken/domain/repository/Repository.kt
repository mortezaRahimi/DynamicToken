package com.myket.farahani.dynamictoken.domain.repository

import com.myket.farahani.dynamictoken.data.remote.model.AppResponse
import com.myket.farahani.dynamictoken.data.remote.model.TokenResponse
import com.myket.farahani.dynamictoken.domain.use_case.GetAppData

interface Repository {
    suspend fun getCalcData(): Result<List<Int>>

    suspend fun getToken(answer: String): Result<String>

    suspend fun getAppData(token:String): Result<AppResponse>
}
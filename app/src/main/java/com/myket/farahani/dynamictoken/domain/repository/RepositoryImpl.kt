package com.myket.farahani.dynamictoken.domain.repository

import com.myket.farahani.dynamictoken.data.remote.ApiService
import com.myket.farahani.dynamictoken.data.remote.model.AppResponse
import com.myket.farahani.dynamictoken.data.remote.model.TokenResponse
import java.lang.Exception

class RepositoryImpl(
    private val api: ApiService
) : Repository {

    override suspend fun getCalcData(): Result<List<Int>> {
        return try {
            val data = api.getCalcData()
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getToken(answer: String): Result<String> {
        return try {
            val token = api.getToken(answer)
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAppData(token: String): Result<AppResponse> {
        return try {
            val data = api.getAppData(token)
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
package com.myket.farahani.dynamictoken.domain.repository

import com.myket.farahani.dynamictoken.data.remote.ApiService
import com.myket.farahani.dynamictoken.data.remote.FileDownloadApi
import com.myket.farahani.dynamictoken.data.remote.model.AppResponse
import okhttp3.ResponseBody
import java.lang.Exception

class RepositoryImpl(
    private val api: ApiService,
    private val downloadApi: FileDownloadApi
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

    override suspend fun downloadFile(url: String): Result<ResponseBody> {
        return try {
            val data = downloadApi.downloadFile(url)
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
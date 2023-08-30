package com.myket.farahani.dynamictoken.domain.use_case

import com.myket.farahani.dynamictoken.data.remote.model.AppResponse
import com.myket.farahani.dynamictoken.domain.repository.Repository
import okhttp3.ResponseBody

class DownloadFile(
    private val repository: Repository
) {

    suspend operator fun invoke(
        token: String
    ): Result<ResponseBody> {
        return repository.downloadFile(token)
    }

}
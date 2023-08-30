package com.myket.farahani.dynamictoken.domain.use_case

import com.myket.farahani.dynamictoken.data.remote.model.AppResponse
import com.myket.farahani.dynamictoken.domain.repository.Repository

class GetAppData(
    private val repository: Repository
) {

    suspend operator fun invoke(
        token: String
    ): Result<AppResponse> {
        return repository.getAppData(token)
    }

}
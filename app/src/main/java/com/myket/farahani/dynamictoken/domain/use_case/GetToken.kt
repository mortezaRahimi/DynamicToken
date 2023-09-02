package com.myket.farahani.dynamictoken.domain.use_case

import com.myket.farahani.dynamictoken.domain.repository.Repository

class GetToken(
    private val repository: Repository
) {

    suspend operator fun invoke(answer: String): Result<String> {
        return repository.getToken(answer)
    }
}
package com.myket.farahani.dynamictoken.domain.use_case

import com.myket.farahani.dynamictoken.domain.repository.Repository

class GetCalcData(
    private val repository:Repository
) {

    suspend operator fun invoke():Result<List<Int>>{
        return repository.getCalcData()
    }

}
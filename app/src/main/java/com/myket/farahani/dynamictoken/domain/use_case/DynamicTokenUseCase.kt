package com.myket.farahani.dynamictoken.domain.use_case

data class DynamicTokenUseCase(
    val getCalcData: GetCalcData,
    val getToken:GetToken,
    val getAppData: GetAppData
)
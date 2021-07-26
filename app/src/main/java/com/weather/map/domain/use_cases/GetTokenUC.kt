package com.weather.map.domain.use_cases

import com.weather.map.domain.common.Result

interface GetTokenUC {
    suspend operator fun invoke(): Result<String>
}
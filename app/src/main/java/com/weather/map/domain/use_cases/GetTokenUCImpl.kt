package com.weather.map.domain.use_cases

import com.weather.map.domain.repositories.UserRepository

class GetTokenUCImpl(private val repo: UserRepository) : GetTokenUC {
    override suspend operator fun invoke() = repo.getToken()

}
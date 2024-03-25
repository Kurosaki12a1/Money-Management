package com.kuro.money.domain.usecase

import com.kuro.money.domain.repository.IconRepository
import javax.inject.Inject

class GetIconUseCase @Inject constructor(
    private val repo : IconRepository
) {
    operator fun invoke() = repo.getListIconFromJSON()
}
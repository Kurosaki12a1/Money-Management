package com.kuro.money.data.repository

import android.content.Context
import com.kuro.money.data.utils.FileUtils
import com.kuro.money.domain.model.IconPack
import com.kuro.money.domain.repository.IconRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class IconRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IconRepository {
    override fun getListIconFromJSON(): Flow<List<String>> = flow {
        try {
            val data = FileUtils.loadJsonFromAsset<IconPack>(context, "package.json")
            val listNameIcon = data?.icons?.keys?.toMutableList()
            emit(listNameIcon ?: listOf())
        } catch (e: Exception) {
            e.printStackTrace()
            emit(listOf(""))
        }
    }.flowOn(dispatcher)
}
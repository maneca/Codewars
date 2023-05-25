package com.example.codewars.data.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.codewars.data.local.CodewarsDatabase
import com.example.codewars.data.local.entity.CompletedChallengeEntity
import com.example.codewars.data.local.entity.RemoteKeyEntity
import com.example.codewars.data.remote.CodewarsApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CodewarsRemoteMediator(
    private val database: CodewarsDatabase,
    private val api: CodewarsApi
) : RemoteMediator<Int, CompletedChallengeEntity>() {
    private val codeWarsDao = database.codewarsDao
    private val remoteKeyDao = database.remoteKeyDao

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CompletedChallengeEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    database.withTransaction {
                        remoteKeyDao.insertOrReplace(RemoteKeyEntity("page", 0))
                    }
                    0
                }
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val nextkeyEntity = database.withTransaction {
                        remoteKeyDao.remoteKeyByQuery("page")
                    }
                    nextkeyEntity.nextKey
                }
            }

            val response = api.getCompletedChallenges(loadKey)
            val challenges = response.body()?.data

            challenges?.let{
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        remoteKeyDao.deleteByQuery("page")
                        codeWarsDao.deleteCompletedChallenges()
                    }

                    remoteKeyDao.insertOrReplace(
                        RemoteKeyEntity("page", loadKey.inc())
                    )
                    codeWarsDao.insertAllCompletedChallenges(it.map { challenge -> challenge.toCompletedChallengeEntity() })
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = loadKey == response.body()?.totalPages
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
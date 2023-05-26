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
import com.example.codewars.utils.DEFAULT_USER
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CodewarsRemoteMediator(
    private val database: CodewarsDatabase,
    private val api: CodewarsApi
) : RemoteMediator<Int, CompletedChallengeEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CompletedChallengeEntity>
    ): MediatorResult {
        val page = when (loadType){
            LoadType.REFRESH -> {
                val remoteKeys = state.anchorPosition?.let { position ->
                    state.closestItemToPosition(position)?.id?.let { repoId ->
                        database.remoteKeyDao().remoteKeyByQuery(repoId)
                    }
                }
                remoteKeys?.nextKey?.dec() ?: 0
            }
            LoadType.PREPEND -> {
                val remoteKeys = state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                    ?.let { repo -> database.remoteKeyDao().remoteKeyByQuery(repo.id) }
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
                    ?.let { repo -> database.remoteKeyDao().remoteKeyByQuery(repo.id) }
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try
        {
            val response = api.getCompletedChallenges(DEFAULT_USER, page)
            val isEndOfList = response.body()?.data?.isEmpty() ?: true

            database.withTransaction {
                val challenges = response.body()?.data ?: emptyList()

                if(loadType == LoadType.REFRESH){
                    database.remoteKeyDao().deleteAll()
                    database.codewarsDao().deleteCompletedChallenges()
                }

                val prevKey = if (page == 0) null else page.dec()
                val nextKey = if (isEndOfList) null else page.inc()
                val keys = challenges.map {
                    RemoteKeyEntity(label = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                database.remoteKeyDao().insertOrReplace(keys)
                database.codewarsDao().insertAllCompletedChallenges(challenges.map { dto -> dto.toCompletedChallengeEntity() })
            }

            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }

    }
}
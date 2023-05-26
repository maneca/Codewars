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
        //1 - Find out what page we need to load from the network, based on the LoadType
        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData){
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try
        {
            //2 - Trigger the network request
            val response = api.getCompletedChallenges(DEFAULT_USER, page)
            val isEndOfList = response.body()?.data?.isEmpty() ?: true
            //3 - Once network request completes, if the received list is not empty then
            database.withTransaction {
                val challenges = response.body()?.data ?: emptyList()
                //3.1 - If LoadType = Refresh -> clear the database
                if(loadType == LoadType.REFRESH){
                    database.remoteKeyDao().deleteAll()
                    database.codewarsDao().deleteCompletedChallenges()
                }
                //3.1 - Compute the remotekeys
                val prevKey = if (page == 0) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = challenges.map {
                    RemoteKeyEntity(label = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                //3.3 - Save the RemoteKeys and Data in the database

                database.remoteKeyDao().insertOrReplace(keys)
                database.codewarsDao().insertAllCompletedChallenges(challenges.map { dto -> dto.toCompletedChallengeEntity() })
            }

            //3.4 - Return Mediator Success
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }

    }

    private suspend fun getKeyPageData(loadType: LoadType,
                                       state: PagingState<Int, CompletedChallengeEntity>): Any {
        return when (loadType){
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 0
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, CompletedChallengeEntity>): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                database.remoteKeyDao().remoteKeyByQuery(repo.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, CompletedChallengeEntity>): RemoteKeyEntity? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                database.remoteKeyDao().remoteKeyByQuery(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, CompletedChallengeEntity>
    ): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.remoteKeyDao().remoteKeyByQuery(repoId)
            }
        }
    }
}
package com.example.codewars.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.codewars.data.local.CodewarsDatabase
import com.example.codewars.data.remote.CodewarsApi
import com.example.codewars.data.remote.mediator.CodewarsRemoteMediator
import com.example.codewars.domain.model.ChallengeDetails
import com.example.codewars.domain.model.CompletedChallenge
import com.example.codewars.domain.repository.CodewarsRepository
import com.example.codewars.utils.CustomExceptions
import com.example.codewars.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class CodewarsRepositoryImp(
   private val database: CodewarsDatabase,
   private val api: CodewarsApi
) : CodewarsRepository{
    @OptIn(ExperimentalPagingApi::class)
    override fun getCompletedChallenges(): Flow<PagingData<CompletedChallenge>> {
        val pagingSourceFactory = {
            database.codewarsDao.getCompletedChallenges()
        }

        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            remoteMediator = CodewarsRemoteMediator(
                api = api, database = database
            ),
            pagingSourceFactory = pagingSourceFactory
        )
            .flow.map { pd -> pd.map { it.toCompletedChallenge() } }

    }

    override suspend fun getChallengeDetails(id: String): Flow<Resource<ChallengeDetails>> = flow {
        try{
            val response = api.getChallengeDetails(id)
            if(response.isSuccessful){
                response.body()?.let {
                    val challengeDetails = it.toChallengeDetails()
                    emit(Resource.Success(challengeDetails))
                }
            }
        }catch (e: HttpException){
            emit(Resource.Error(
                exception = CustomExceptions.NoInternetConnectionException(),
                data = null
            ))
        }catch (e: IOException){
            emit(Resource.Error(
                exception = CustomExceptions.ApiNotResponding(),
                data = null
            ))
        }
    }
}
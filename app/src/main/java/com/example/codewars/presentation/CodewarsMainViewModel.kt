package com.example.codewars.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.codewars.domain.model.CompletedChallenge
import com.example.codewars.domain.repository.CodewarsRepository
import com.example.codewars.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class CodewarsMainViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val repository: CodewarsRepository
): ViewModel(){

    private val _completedChallenges = repository.getCompletedChallenges()
        .flowOn(dispatcher.io())
        .cachedIn(viewModelScope)

    val completedChallenges: Flow<PagingData<CompletedChallenge>>
        get() = _completedChallenges

}
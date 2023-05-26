package com.example.codewars.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codewars.domain.repository.CodewarsRepository
import com.example.codewars.presentation.states.ChallengeState
import com.example.codewars.presentation.states.UiEvent
import com.example.codewars.utils.ARGUMENT_KEY
import com.example.codewars.utils.CustomExceptions
import com.example.codewars.utils.DispatcherProvider
import com.example.codewars.utils.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dispatcher: DispatcherProvider,
    private val repository: CodewarsRepository
): ViewModel(){

    private val _state = MutableStateFlow(ChallengeState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
        val eventFlow = _eventFlow.asSharedFlow()

    init {
        val id : String? = savedStateHandle.get<String>(ARGUMENT_KEY)?.fromJson()
        getChallengeDetails(id)
    }

    private fun getChallengeDetails(id: String?){
        viewModelScope.launch {
            if(!id.isNullOrEmpty()){
                _eventFlow.emit(UiEvent.Loading)
                repository
                    .getChallengeDetails(id)
                    .flowOn(dispatcher.io())
                    .collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                _state.value = state.value.copy(
                                    challengeDetails = result.data,
                                    exception = null
                                )
                            }
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    challengeDetails = result.data,
                                    exception = result.exception ?: CustomExceptions.UnknownException
                                )
                                _eventFlow.emit(UiEvent.Failed)
                            }
                            else -> {}
                        }
                    }
            }else{
                _eventFlow.emit(UiEvent.Failed)
            }

        }
    }

    private inline fun<reified T> String.fromJson(): T{
        return Gson().fromJson(this, T::class.java)
    }
}
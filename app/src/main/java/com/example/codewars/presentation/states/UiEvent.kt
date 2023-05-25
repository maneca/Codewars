package com.example.codewars.presentation.states

sealed class UiEvent{

    object Loading : UiEvent()
    object DetailsLoaded: UiEvent()
    object Failed : UiEvent()
    object NoInternetConnection : UiEvent()
}
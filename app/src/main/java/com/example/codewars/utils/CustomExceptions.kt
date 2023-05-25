package com.example.codewars.utils

import java.io.IOException

sealed class CustomExceptions: IOException(){

    class NoInternetConnectionException : CustomExceptions()

    object UnknownException : CustomExceptions()

    class ApiNotResponding : CustomExceptions()
}
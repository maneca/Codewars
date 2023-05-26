package com.example.codewars.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.toCorrectFormat(): String{
    val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val date1 = LocalDateTime.parse(this, dtf)
    return date1.format(DateTimeFormatter.ofPattern("dd-MM-yyyy 'at' HH:mm"))
}
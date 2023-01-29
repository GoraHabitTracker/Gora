package com.pethabittracker.gora.presentation.models

//sealed class Priority {
//    val defaultPriority = 0
//    val donePriority = 1
//    val skipPriority = 2
//}

enum class Priority(val value: Int) {
    Default(0),
    Done(1),
    Skip(2)
}
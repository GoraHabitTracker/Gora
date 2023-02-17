package com.pethabittracker.gora.presentation.models

enum class Priority(val value: Int) {
    Default(0),
    Done(1),
    Skip(2),
    Inactive(3)
}
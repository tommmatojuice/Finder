package com.example.projectfinder.ui.DBClasses

import java.io.Serializable

data class Login(
    val password: String,
    val username: String,
    val fingerPrint: String
) : Serializable
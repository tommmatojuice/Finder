package com.example.projectfinder.ui.DBClasses

import java.io.Serializable

data class Tokens(
    val accessToken: String,
    val refreshToken: String
) : Serializable
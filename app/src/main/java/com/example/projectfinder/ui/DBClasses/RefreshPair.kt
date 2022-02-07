package com.example.projectfinder.ui.DBClasses

import java.io.Serializable

data class RefreshPair(
    val fingerPrint: String,
    val refreshToken: String
) : Serializable
package com.example.projectfinder.ui.DBClasses

import java.io.Serializable

data class Contact(
    val email: String,
    val telegram: String,
    val website: String
) : Serializable
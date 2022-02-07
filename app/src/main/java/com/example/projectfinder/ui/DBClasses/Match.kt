package com.example.projectfinder.ui.DBClasses

import java.io.Serializable

data class Match(
    val username: String,
    val slug: String,
    val projectTitle: String,
    val likeFromUser: Boolean,
    val likeFromProject: Boolean
) : Serializable
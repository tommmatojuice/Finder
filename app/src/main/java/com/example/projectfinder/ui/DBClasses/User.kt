package com.example.projectfinder.ui.DBClasses

import java.io.Serializable

data class User(
    val password: String?,
    val skillTags: List<SkillTag>,
    val username: String,
    val name: String,
    val lastname: String,
    val contact: Contact,
    val gender: Boolean,
    val birthDate: String,
    val location: String,
    val information: String,
    val avatarUrl: String,
    val coverUrl: String
) : Serializable
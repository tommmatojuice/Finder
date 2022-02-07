package com.example.projectfinder.ui.DBClasses

import java.io.Serializable

data class ProjectsItem(
    val skillTags: List<SkillTag>?,
    val title: String,
    val description: String,
    val location: String,
    val canRemote: Boolean,
    val slug: String,
    val user: User,
    val avatarUrl: String?,
    val coverUrl: String?
) : Serializable
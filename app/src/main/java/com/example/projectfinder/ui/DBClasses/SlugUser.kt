package com.example.projectfinder.ui.DBClasses

import java.io.Serializable

data class SlugUser (
    var username: String,
    var slug: String
) : Serializable
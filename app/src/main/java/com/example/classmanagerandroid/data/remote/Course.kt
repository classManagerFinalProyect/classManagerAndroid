package com.example.classmanagerandroid.data.remote

import com.example.classmanagerandroid.data.local.RolUser

data class Course (
    val users: MutableList<RolUser>,
    val classes: MutableList<String>,
    val name: String,
    val description: String,
    var id: String
)

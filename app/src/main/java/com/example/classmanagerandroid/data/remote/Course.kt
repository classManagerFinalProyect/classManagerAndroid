package com.example.classmanagerandroid.data.remote

import com.example.classmanagerandroid.data.local.RolUser

data class Course (
    val users: MutableList<RolUser>,
    val classes: MutableList<String>,
    val events: MutableList<String>,
    var name: String,
    var description: String,
    var id: String,
    var img: String
)

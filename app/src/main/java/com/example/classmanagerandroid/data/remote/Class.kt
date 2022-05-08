package com.example.classmanagerandroid.data.remote

import com.example.classmanagerandroid.data.local.RolUser

data class Class (
    var id: String,
    var name: String,
    var description: String,
    val idPractices: MutableList<String>,
    val users: MutableList<RolUser>,
    val idOfCourse: String,
    var img: String
)

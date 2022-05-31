package com.example.classmanagerandroid.data.remote

data class AppUser (
    var email: String,
    var imgPath: String,
    var name: String,
    val courses: MutableList<String>,
    val classes: MutableList<String>,
    val id: String,
    var description: String,
    val password: String
)

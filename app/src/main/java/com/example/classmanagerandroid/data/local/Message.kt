package com.example.classmanagerandroid.data.local

import com.example.classmanagerandroid.data.remote.appUser

data class Message (
    val message: String,
    val sentBy: appUser,
    val sentOn: String
)

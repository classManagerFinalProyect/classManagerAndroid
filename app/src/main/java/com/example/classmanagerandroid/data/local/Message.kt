package com.example.classmanagerandroid.data.local

import com.example.classmanagerandroid.data.remote.AppUser

data class Message (
    val message: String,
    val sentBy: AppUser,
    val sentOn: String
)

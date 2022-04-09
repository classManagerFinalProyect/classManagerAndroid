package com.example.classmanagerandroid.data.remote

import com.example.classmanagerandroid.data.local.Message

data class Chat (
    val id: String,
    val conversation: MutableList<Message>
)

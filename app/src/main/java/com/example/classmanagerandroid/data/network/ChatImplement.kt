package com.example.classmanagerandroid.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class ChatImplement {
    companion object {
        private var auth: FirebaseAuth = Firebase.auth
        private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
        private var storage = Firebase.storage
        private var storageReference: StorageReference = storage.reference


        fun deleteChatById(
            idOfChat: String,
            onFinished: (Boolean) -> Unit
        ) {
            db.collection("practicesChats")
                .document(idOfChat)
                .delete()
                .addOnSuccessListener {
                    onFinished(true)
                }
        }
    }
}
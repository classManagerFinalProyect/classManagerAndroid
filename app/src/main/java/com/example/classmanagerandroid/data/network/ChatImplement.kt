package com.example.classmanagerandroid.data.network

import com.google.firebase.firestore.FirebaseFirestore


class ChatImplement {
    companion object {
        private var db: FirebaseFirestore = FirebaseFirestore.getInstance()


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
package com.example.classmanagerandroid.data.network

import com.example.classmanagerandroid.data.remote.appUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class UsersImplement {
    companion object {
        private var auth: FirebaseAuth = Firebase.auth
        private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
        private var storage = Firebase.storage
        private var storageReference: StorageReference = storage.reference

        fun deleteUserById(
            idOfUser: String,
            onFinished: (Boolean) -> Unit
        ) {
            db.collection("users")
                .document(idOfUser)
                .delete()
                .addOnSuccessListener {
                    onFinished(true)
                }
        }


        fun updateUser(
            idOfUser: String,
            user: appUser,
            onFinished: (Boolean) -> Unit
        ) {
            db.collection("users")
                .document(idOfUser)
                .set(user)
                .addOnSuccessListener {
                    onFinished(true)
                }
        }

        fun getUserById(
            idOfUser: String,
            onFinished: (Boolean, appUser) -> Unit
        ) {
            db.collection("users")
                .document(idOfUser)
                .get()
                .addOnSuccessListener {
                   val user = appUser(
                        id = it.get("id") as String,
                        description = it.get("description") as String,
                        name = it.get("name") as String,
                        email = it.get("email") as String,
                        imgPath = it.get("imgPath") as String,
                        classes = it.get("classes") as MutableList<String>,
                        courses = it.get("courses") as MutableList<String>
                    )
                    onFinished(true, user)
                }
        }
    }
}
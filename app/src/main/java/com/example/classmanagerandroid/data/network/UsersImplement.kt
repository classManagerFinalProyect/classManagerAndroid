package com.example.classmanagerandroid.data.network

import com.example.classmanagerandroid.data.remote.AppUser
import com.google.firebase.firestore.FirebaseFirestore


class UsersImplement {
    companion object {
        private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

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
            user: AppUser,
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
            onFinished: (Boolean, AppUser) -> Unit
        ) {
            db.collection("users")
                .document(idOfUser)
                .get()
                .addOnSuccessListener {
                   val user = AppUser(
                        id = it.get("id") as String,
                        description = it.get("description") as String,
                        name = it.get("name") as String,
                        email = it.get("email") as String,
                        imgPath = it.get("imgPath") as String,
                        classes = it.get("classes") as MutableList<String>,
                        courses = it.get("courses") as MutableList<String>,
                        password = it.get("password") as String
                    )
                    onFinished(true, user)
                }
        }
    }
}

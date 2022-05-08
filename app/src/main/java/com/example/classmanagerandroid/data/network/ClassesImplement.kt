package com.example.classmanagerandroid.data.network

import com.example.classmanagerandroid.data.local.RolUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.example.classmanagerandroid.data.remote.Class
import com.example.classmanagerandroid.data.remote.Course
import com.example.classmanagerandroid.data.remote.Event

class ClassesImplement {

    companion object {
        private var auth: FirebaseAuth = Firebase.auth
        private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
        private var storage = Firebase.storage
        private var storageReference: StorageReference = storage.reference

        fun createNewClass(
            newClass: Class,
            onFinished: (Boolean, Class) -> Unit
        ) {
            val document = db.collection("classes").document()
            newClass.id = document.id

            document.set(newClass)
                .addOnSuccessListener {
                    onFinished(true,newClass)
                }
        }

        fun getClassById(
            idOfClass: String,
            onFinished: (Boolean, Class) -> Unit
        ) {
            db.collection("classes")
                .document(idOfClass)
                .get()
                .addOnSuccessListener {
                    val users = it.get("users") as  MutableList<HashMap<String,String>>
                    val listOfRolUser: MutableList<RolUser> = mutableListOf()
                    users.forEach { rolUser ->
                        listOfRolUser.add(
                            RolUser(
                                id = rolUser.get("id") as String,
                                rol = rolUser.get("rol") as String
                            )
                        )
                    }

                    val newClass = Class(
                        name = it.get("name") as String,
                        idPractices = it.get("idPractices") as MutableList<String>,
                        users = listOfRolUser,
                        description = it.get("description") as String,
                        id = it.id,
                        idOfCourse = it.get("idOfCourse") as String,
                        img = it.get("img") as String
                    )
                    onFinished(true,newClass)
                }
        }

        fun updateClass(
            newClass: Class,
            onFinished: (Boolean, Class) -> Unit
        ) {
            db.collection("classes")
                .document(newClass.id)
                .set(newClass)
                .addOnSuccessListener {
                    onFinished(true, newClass)
                }
        }
    }
}
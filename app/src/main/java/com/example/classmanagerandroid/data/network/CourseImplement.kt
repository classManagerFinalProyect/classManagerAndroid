package com.example.classmanagerandroid.data.network

import com.example.classmanagerandroid.data.remote.Course
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class CourseImplement {
    companion object {
        private var auth: FirebaseAuth = Firebase.auth
        private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
        private var storage = Firebase.storage
        private var storageReference: StorageReference = storage.reference

        fun createNewCourse(
            newCourse: Course,
            onFinished: (Boolean, Course) -> Unit
        ) {
            val document = db.collection("course").document()
            newCourse.id = document.id
            document.set(newCourse)
                .addOnSuccessListener {
                    onFinished(true, newCourse)
                }
        }

        fun updateCourse(
            newCourse: Course,
            onFinished: (Boolean, Course) -> Unit
        ) {
            AccesToDataBase.db.collection("course")
                .document(newCourse.id)
                .set(newCourse)
                .addOnSuccessListener {
                    onFinished(true, newCourse)
                }
        }

    }
}
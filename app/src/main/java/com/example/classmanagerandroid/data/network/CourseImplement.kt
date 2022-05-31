package com.example.classmanagerandroid.data.network

import com.example.classmanagerandroid.data.remote.Course
import com.google.firebase.firestore.FirebaseFirestore


class CourseImplement {
    companion object {
        private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

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
            AccessToDataBase.db.collection("course")
                .document(newCourse.id)
                .set(newCourse)
                .addOnSuccessListener {
                    onFinished(true, newCourse)
                }
        }

    }
}
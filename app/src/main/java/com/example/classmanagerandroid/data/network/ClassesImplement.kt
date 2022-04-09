package com.example.classmanagerandroid.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.example.classmanagerandroid.data.remote.Class

class ClassesImplement {

    companion object {
        var auth: FirebaseAuth = Firebase.auth
        var db: FirebaseFirestore = FirebaseFirestore.getInstance()
        var storage = Firebase.storage
        var storageReference: StorageReference = storage.reference

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
    }
}
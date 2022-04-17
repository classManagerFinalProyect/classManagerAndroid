package com.example.classmanagerandroid.data.network

import android.widget.Toast
import com.example.classmanagerandroid.Classes.CurrentUser
import com.example.classmanagerandroid.data.remote.Event
import com.example.classmanagerandroid.data.remote.appUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class EventImplement {


    companion object {
        private var auth: FirebaseAuth = Firebase.auth
        private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
        private var storage = Firebase.storage
        private var storageReference: StorageReference = storage.reference

        fun uploadEvent(
            newEvent: Event,
            onFinished: (Boolean, Event) -> Unit
        ) {
            val document = db.collection("event").document()
            newEvent.id = document.id

            document.set(newEvent)
                .addOnSuccessListener {
                    onFinished(true,newEvent)
                }
        }

        fun deleteEventById(
            idOfEvent: String,
            onFinished: (Boolean) -> Unit
        ) {
            AccesToDataBase.db.collection("event")
                .document(idOfEvent)
                .delete()
                .addOnSuccessListener {
                    onFinished(true)
                }
        }

        fun getEventById(
            idOfEvent: String,
            onFinished: (Boolean, Event) -> Unit
        ) {
            db.collection("event")
                .document(idOfEvent)
                .get()
                .addOnSuccessListener {
                     val newEvent = Event(
                         date = it.get("date") as String,
                         finalTime = it.get("finalTime") as String,
                         id = it.get("id") as String,
                         idOfCourse = it.get("idOfCourse") as String,
                         initialTime = it.get("initialTime") as String,
                         name = it.get("name") as String,
                         nameOfClass = it.get("nameOfClass") as String
                     )
                    onFinished(true,newEvent)
                }
        }

        fun updateEvent(
            idOfEvent: String,
            event: Event,
            onFinished: (Boolean) -> Unit
        ) {
            db.collection("event")
                .document(idOfEvent)
                .set(event)
                .addOnSuccessListener {
                    onFinished(true)
                }
        }
    }
}
package com.example.classmanagerandroid.data.network

import com.example.classmanagerandroid.data.remote.Event
class EventImplement {
    companion object {
        fun uploadEvent(
            newEvent: Event,
            onFinished: (Boolean, Event) -> Unit
        ) {
            val document = CourseImplement.db.collection("event").document()
            newEvent.id = document.id

            document.set(newEvent)
                .addOnSuccessListener {
                    onFinished(true,newEvent)
                }
        }
    }
}
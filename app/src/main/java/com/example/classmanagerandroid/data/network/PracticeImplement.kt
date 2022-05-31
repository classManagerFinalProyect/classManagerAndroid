package com.example.classmanagerandroid.data.network


import com.google.firebase.firestore.FirebaseFirestore


class PracticeImplement {
    companion object {
        private var db: FirebaseFirestore = FirebaseFirestore.getInstance()


        fun deletePracticeById(
            idOfPractice: String,
            onFinished: (Boolean) -> Unit
        ) {
            db.collection("practices")
                .document(idOfPractice)
                .delete()
                .addOnSuccessListener {
                    onFinished(true)
                }
        }
    }
}
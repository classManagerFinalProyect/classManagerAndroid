package com.example.classmanagerandroid.data.network

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.data.remote.Practice
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class AccessToDataBase {
    companion object {
         var auth: FirebaseAuth = Firebase.auth
         val db: FirebaseFirestore = FirebaseFirestore.getInstance()
         var storage = Firebase.storage
         var storageReference: StorageReference = storage.reference
         val storageInstance = FirebaseStorage.getInstance()




        fun deletePracticeByPractice (
            context: Context,
            navController: NavController,
            selectedPractice: Practice
        ) {
            db.collection("practices")
                .document(selectedPractice.id)
                .delete()
                .addOnSuccessListener {
                    deletePracticeChatById(selectedPractice.idOfChat)

                    CurrentUser.myClasses.forEach {
                        if(it.id == selectedPractice.idOfClass) {
                            it.idPractices.remove(selectedPractice.id)
                            deleteIdOfPracticeInTableClass(
                                newValueOfPractices = it.idPractices,
                                selectedPractice = selectedPractice
                            )
                        }
                    }
                    CurrentUser.uploadCurrentUser(
                        onFinished = {
                            navController.popBackStack()
                        }
                    )
                    Toast.makeText(context,"La práctica se ha eliminado correctamente", Toast.LENGTH_SHORT).show()
                }
        }

        fun deleteIdOfPracticeInTableClass (
            newValueOfPractices: MutableList<String>,
            selectedPractice: Practice
        ) {
            db.collection("classes")
                .document(selectedPractice.idOfClass)
                .update("idPractices",newValueOfPractices)
        }

        fun deletePracticeChatById (
            idOfChat: String
        ) {
            db.collection("practicesChats")
                .document(idOfChat)
                .delete()
                .addOnSuccessListener {
                }
        }
    }
}
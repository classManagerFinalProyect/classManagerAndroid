package com.example.classmanagerandroid.Screens.Register

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.classmanagerandroid.Classes.CurrentUser
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.Screens.Utils.isValidEmail
import com.example.classmanagerandroid.Screens.Utils.isValidPassword
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.auth
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.db
import com.example.classmanagerandroid.data.network.UsersImplement.Companion.getUserById

class MainViewModelRegister: ViewModel() {

     fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        context: Context,
        navController: NavController,
        onFinished: () -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")

                    Toast.makeText(context,"Has sido registrado correctamente", Toast.LENGTH_LONG).show()
                    setInformationUser(
                        email = email,
                        navController = navController,
                        onFinished = {
                            onFinished()
                        }
                    )
                } else {
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context,"El usuario no ha podido ser creado", Toast.LENGTH_LONG).show()
                    onFinished()
                }
            }
    }

    private fun setInformationUser(
        email: String,
        navController: NavController,
        onFinished: () -> Unit
    ) {

        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .set(
                hashMapOf(
                    "courses" to  mutableListOf<String>(),
                    "classes" to mutableListOf<String>(),
                    "name" to "userName",
                    "email" to email,
                    "imgPath" to "gs://class-manager-58dbf.appspot.com/user/defaultUserImg.png",
                    "id" to auth.currentUser?.uid.toString(),
                    "description" to "myDescription"
                )
            )
            .addOnCompleteListener {
                if(it.isSuccessful){
                    saveCurrentUser(
                        onFinished = {
                            onFinished()
                            navController.navigate(Destinations.MainAppView.route)
                        }
                    )
                }
                else {
                    onFinished()
                    Log.d("Error de registro","No se ha podido crear el usuario")
                }

            }
    }

    fun saveCurrentUser(
        onFinished: () -> Unit
    ) {
        getUserById(
            idOfUser = auth.currentUser?.uid.toString(),
            onFinished = { finish, user ->
                if(finish) {
                    CurrentUser.currentUser = user
                    CurrentUser.updateDates(
                        onFinished = {
                            onFinished()
                        }
                    )
                }
                else {
                    Log.d("Error tip get","No se ha podido obtener el usuario")
                }
            }
        )
    }



    fun checkAllValidations(
        textEmail: String,
        textPassword: String,
        checkedStatePrivacyPolicies: Boolean
    ): Boolean {
        if (
            !isValidEmail(text = textEmail) ||
            !isValidPassword(text = textPassword) ||
            !checkedStatePrivacyPolicies
        )  return false
        return  true
    }
}
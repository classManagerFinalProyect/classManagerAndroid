package com.example.classmanagerandroid.Views.Register

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.classmanagerandroid.Classes.CurrentUser
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.data.network.AccesToDataBase.Companion.auth
import com.example.classmanagerandroid.data.network.AccesToDataBase.Companion.db
import com.example.classmanagerandroid.data.network.UsersImplement.Companion.getUserById
import com.example.classmanagerandroid.data.remote.appUser
import java.util.regex.Pattern

class MainViewModelRegister: ViewModel() {

     fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        context: Context,
        navController: NavController
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")

                    Toast.makeText(context,"Has sido registrado correctamente", Toast.LENGTH_LONG).show()
                    setInformationUser(
                        email = email,
                        navController = navController
                    )
                } else {
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context,"El usuario no ha podido ser creado", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun setInformationUser(
        email: String,
        navController: NavController
    ) {

        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .set(
                hashMapOf(
                    "courses" to  mutableListOf<String>(),
                    "classes" to mutableListOf<String>(),
                    "name" to "userName",
                    "email" to email,
                    "imgPath" to "https://firebasestorage.googleapis.com/v0/b/class-manager-58dbf.appspot.com/o/appImages%2FdefaultUserImg.png?alt=media&token=eb869349-7d2b-4b9a-b04a-b304c0366c78",
                    "id" to auth.currentUser?.uid.toString(),
                    "description" to "myDescription"
                )
            )
            .addOnSuccessListener {
                saveCurrentUser(
                    onFinished = {
                        navController.navigate(Destinations.MainAppView.route)
                    }
                )
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



    //Validaciones
    fun isValidEmail(text: String) = Pattern.compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$", Pattern.CASE_INSENSITIVE).matcher(text).find()
    fun isValidPassword(text: String) = Pattern.compile("^[a-zA-Z0-9_]{8,}\$",Pattern.CASE_INSENSITIVE).matcher(text).find()

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
package com.example.classmanagerandroid.Screens.Register

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.Screens.Utils.createSha256
import com.example.classmanagerandroid.Screens.Utils.isValidEmail
import com.example.classmanagerandroid.Screens.Utils.isValidPassword
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.auth
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.db
import com.example.classmanagerandroid.data.network.UsersImplement.Companion.getUserById
import com.example.classmanagerandroid.data.remote.AppUser

class MainViewModelRegister: ViewModel() {

     fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        context: Context,
        navController: NavController,
        onFinished: (Boolean) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")

                    Toast.makeText(context,"Has sido registrado correctamente", Toast.LENGTH_LONG).show()
                    setInformationUser(
                        email = email,
                        password = password,
                        navController = navController,
                        onFinished = {
                            onFinished(true)
                        }
                    )
                } else {
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context,"", Toast.LENGTH_LONG).show()
                    onFinished(false)
                }
            }
    }

    private fun setInformationUser(
        email: String,
        password: String,
        navController: NavController,
        onFinished: () -> Unit
    ) {
        val newUser = AppUser(
            description = "myDescription",
            name = "userName",
            id = auth.currentUser?.uid.toString(),
            email = email,
            imgPath = "gs://class-manager-58dbf.appspot.com/user/defaultUserImg.png",
            courses = mutableListOf(),
            classes = mutableListOf(),
            password = createSha256(base = password)!!
        )

        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .set(newUser)
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

    private fun saveCurrentUser(
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
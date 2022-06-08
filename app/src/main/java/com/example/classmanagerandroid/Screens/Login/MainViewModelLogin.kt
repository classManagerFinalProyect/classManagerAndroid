package com.example.classmanagerandroid.Screens.Login

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.Navigation.navController
import com.example.classmanagerandroid.Screens.Utils.createSha256
import com.example.classmanagerandroid.data.remote.AppUser
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class MainViewModelLogin: ViewModel() {
    var auth  = Firebase.auth
    var db = FirebaseFirestore.getInstance()

    fun saveCurrentUser(onFinished: () -> Unit) {
        db.collection("users").get().addOnSuccessListener {
            for (document in it) {
                if(document.id == auth.currentUser?.uid.toString()) {
                    val currentUser = AppUser(
                        id = document.id,
                        name = document.get("name") as String,
                        email = document.get("email") as String,
                        imgPath = document.get("imgPath") as String,
                        courses = document.get("courses") as MutableList<String>,
                        classes = document.get("classes") as MutableList<String>,
                        description = document.get("description") as String,
                        password = document.get("password") as String
                    )
                    CurrentUser.currentUser = currentUser
                    CurrentUser.updateDates(
                        onFinished = {
                            onFinished()
                        }
                    )
                }
            }
        }
    }

    fun finishLogin(accountTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = accountTask.getResult(ApiException::class.java)
                account?.idToken?.let { token ->
                    val credential = GoogleAuthProvider.getCredential(token, null)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                checkIfIsNewAccount(account)
                            }
                            else {
                                Log.w(ContentValues.TAG, "Google sign in failed")
                            }

                        }
                }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, "Google sign in failed", e)
        }
    }

    private fun checkIfIsNewAccount(
        account: GoogleSignInAccount
    ) {
        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .get()
            .addOnCompleteListener {
                if (it.result.exists())
                    saveCurrentUser { navController.navigate(Destinations.MainAppView.route) }
                else
                    setInformationUser(account)
            }
    }

    private fun setInformationUser(
        account: GoogleSignInAccount
    ) {

        val newUser = AppUser(
            description = "myDescription",
            name = account.displayName!!,
            id = auth.currentUser?.uid.toString(),
            email = account.email!!,
            imgPath = "gs://class-manager-58dbf.appspot.com/user/defaultUserImg.png",
            courses = mutableListOf(),
            classes = mutableListOf(),
            password = createSha256(base = account.email!!)!!
        )

        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .set(newUser)
        saveCurrentUser {
            navController.navigate(Destinations.MainAppView.route)
        }
    }

    fun signIn(
        email: String,
        password: String,
        mainViewModelLogin: MainViewModelLogin,
        context: Context,
        navController: NavController,
        onFinished: () -> Unit
    ) {

         mainViewModelLogin.auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Inicio de sesión", "Se ha iniciado la sesión")
                Toast.makeText(context,"Usted se ha logeado correctamente", Toast.LENGTH_SHORT).show()
                mainViewModelLogin.saveCurrentUser {
                    navController.navigate(Destinations.MainAppView.route)
                }

            } else {
                onFinished()
                Log.w("Inicio de sesión", "No se ha podido iniciar la sesión", task.exception)
                Toast.makeText(context, "El usuario o contraseña no son válidos.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
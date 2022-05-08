package com.example.classmanagerandroid.Screens.Login.ForgotPassword

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.auth

class MainViewModelForgotPassword: ViewModel() {


    fun sendEmailToChangePassword(
        context: Context,
        emailText: String
    ) {
        auth.sendPasswordResetEmail(emailText)
            .addOnCompleteListener { task ->
                Toast.makeText(context, "Le hemos enviado un correo", Toast.LENGTH_SHORT).show()

                if (task.isSuccessful)
                    Toast.makeText(context, "La contraseña ha sido cambiada", Toast.LENGTH_SHORT).show()
            }
    }

}
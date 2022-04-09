package com.example.classmanagerandroid.Views.Login.ForgotPassword

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.classmanagerandroid.data.network.AccesToDataBase.Companion.auth
import java.util.regex.Pattern

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
    fun isValidEmail(text: String) = Pattern.compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$", Pattern.CASE_INSENSITIVE).matcher(text).find()

}
package com.example.classmanagerandroid.Screens.Settings

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.classmanagerandroid.Classes.CurrentUser
import com.example.classmanagerandroid.data.network.AccessToDataBase
import com.example.classmanagerandroid.data.network.UsersImplement.Companion.deleteUserById
import com.example.classmanagerandroid.data.network.UsersImplement.Companion.updateUser

class ViewModelSettings: ViewModel() {

    fun updateCurrentUser(
        onFinished: () -> Unit
    ){
        updateUser(
            idOfUser = CurrentUser.currentUser.id,
            user = CurrentUser.currentUser,
            onFinished = {
                onFinished()
            }
        )

    }
    fun deleteUser(
        context: Context,
        onFinished: () -> Unit
    ){
        deleteUserById(
            idOfUser = AccessToDataBase.auth.currentUser!!.uid,
            onFinished = {

                AccessToDataBase.auth.currentUser!!.delete()
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context,"La cuenta ha sido eliminada correctamente", Toast.LENGTH_SHORT).show()
                            onFinished()
                        }
                    }
            }
        )
    }
}
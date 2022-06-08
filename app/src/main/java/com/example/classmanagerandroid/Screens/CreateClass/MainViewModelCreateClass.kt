package com.example.classmanagerandroid.Screens.CreateClass

import android.content.Context
import android.content.IntentSender
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.data.local.RolUser
import com.example.classmanagerandroid.data.remote.Class
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.auth
import com.example.classmanagerandroid.data.network.ClassesImplement.Companion.createNewClass
import com.example.classmanagerandroid.data.network.CourseImplement.Companion.updateCourse
import com.example.classmanagerandroid.data.network.UsersImplement.Companion.updateUser
import com.example.classmanagerandroid.data.remote.Course

class MainViewModelCreateClass: ViewModel() {

    fun createClass(
        textDescription: String,
        textNameOfClass: String,
        itemSelectedCurse: Course,
        onFinished: () -> Unit
    ) {

        val newClass = Class(
            id = "",
            name = textNameOfClass,
            description = textDescription,
            idPractices = mutableListOf(),
            idOfCourse = itemSelectedCurse.id,
            users =  mutableListOf(
                RolUser(
                    id = "${auth.currentUser?.uid}",
                    rol = "admin"
                )
            ),
            img = "gs://class-manager-58dbf.appspot.com/appImages/books.jpg"
        )

        createNewClass(
            newClass = newClass,
            onFinished = { success, newClass ->
                if (success) {
                    if(itemSelectedCurse.name != "Sin asignar") {
                        itemSelectedCurse.classes.add(newClass.id)
                        updateCourse(
                            newCourse = itemSelectedCurse,
                            onFinished = { _, _ ->
                                CurrentUser.currentUser.classes.add(newClass.id)
                                updateUser(
                                    idOfUser = auth.currentUser?.uid.toString(),
                                    user = CurrentUser.currentUser,
                                    onFinished = {
                                        if (it) {
                                            CurrentUser.updateDates(
                                                onFinished = {
                                                    onFinished()
                                                 }
                                            )
                                        }
                                    }
                                )
                            }
                        )
                    }
                    else {
                        CurrentUser.currentUser.classes.add(newClass.id)
                        updateUser(
                            idOfUser = auth.currentUser?.uid.toString(),
                            user = CurrentUser.currentUser,
                            onFinished = {
                                if (it) {
                                    CurrentUser.updateDates(
                                        onFinished = {
                                            onFinished()
                                        }
                                    )
                                }
                            }
                        )
                    }
                }
            }
        )
    }

}
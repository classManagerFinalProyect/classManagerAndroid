package com.example.classmanagerandroid.Screens.CreateClass

import android.content.Context
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

    fun getNamesOfCurses(): MutableList<String> {
        val namesOfCurses = mutableListOf<String>()
        CurrentUser.myCourses.forEach {
            namesOfCurses.add(it.name)
        }
        return namesOfCurses
    }

    fun createClass(
        navController: NavController,
        context: Context,
        textDescription: String,
        textNameOfClass: String,
        itemSelectedCurse: Course
    ) {
       /*val document = db.collection("classes").document()
        val idOfDocument = document.id*/


        val newClass = Class(
            id = "",
            name = textNameOfClass,
            description = textDescription,
            idPractices = mutableListOf<String>(),
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
                    if(!itemSelectedCurse.name.equals("Sin asignar") ) {
                        itemSelectedCurse.classes.add(newClass.id)
                        updateCourse(
                            newCourse = itemSelectedCurse,
                            onFinished = { success, newCourse ->
                                CurrentUser.currentUser.classes.add(newClass.id)
                                updateUser(
                                    idOfUser = auth.currentUser?.uid.toString(),
                                    user = CurrentUser.currentUser,
                                    onFinished = {
                                        if (it) {
                                            CurrentUser.updateDates(
                                                onFinished = {
                                                    Toast.makeText(context,"El curso ha sido creado correctamente", Toast.LENGTH_SHORT).show()
                                                    navController.navigate(Destinations.MainAppView.route)
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
                                            Toast.makeText(context,"El curso ha sido creado correctamente", Toast.LENGTH_SHORT).show()
                                            navController.navigate(Destinations.MainAppView.route)
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
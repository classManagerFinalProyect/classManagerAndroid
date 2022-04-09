package com.example.classmanagerandroid.Views.CreateCourse


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.classmanagerandroid.Classes.CurrentUser
import com.example.classmanagerandroid.data.local.RolUser
import com.example.classmanagerandroid.data.network.AccesToDataBase.Companion.auth
import com.example.classmanagerandroid.data.network.AccesToDataBase.Companion.db
import com.example.classmanagerandroid.data.network.CourseImplement.Companion.createNewCourse
import com.example.classmanagerandroid.data.network.UsersImplement.Companion.updateUser
import com.example.classmanagerandroid.data.remote.Course
import com.example.classmanagerandroid.data.remote.ListItem

class MainViewModelCreateCourse: ViewModel() {

    var allListItems = mutableListOf<ListItem>()
    val allNameOfAvailablesClasses =   mutableListOf<String>()

    fun createCourse(
        textNameOfCourse: String,
        textOfDescription: String,
        navController: NavController,
        context: Context
    ) {
        var mySelectedClasses = mutableListOf<String>()
        allListItems.forEach {
            if (it.isSelected)  mySelectedClasses.add(it.id)
        }

        val newCourse = Course(
            name = textNameOfCourse,
            description = textOfDescription,
            id = "",
            classes = mySelectedClasses,
            users = mutableListOf<RolUser>(
                RolUser(
                    id = "${auth.currentUser?.uid}",
                    rol = "admin"
                )
            )
        )

        createNewCourse(
            newCourse = newCourse,
            onFinished = { success , newCourse ->
                if (success) {
                    CurrentUser.currentUser.courses.add(newCourse.id)
                    updateUser(
                        idOfUser = auth.currentUser?.uid.toString(),
                        user = CurrentUser.currentUser,
                        onFinished = { success ->
                            if (success) {
                                CurrentUser.updateDates()
                                Toast.makeText(context,"El curso ha sido creado correctamente", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        }
                    )
                    mySelectedClasses.forEach{
                        db.collection("classes")
                            .document(it)
                            .update(
                                "idOfCourse" , newCourse.id
                            )
                    }

                }
            }
        )


    }

    fun createListItems() {
        allListItems.clear()
        CurrentUser.myClasses.forEach {
            if (it.idOfCourse.equals("Sin asignar")) {
                allListItems.add(
                    ListItem(
                        title = it.name,
                        isSelected = false,
                        id = it.id
                    )
                )
            }
        }
    }
    fun getOfListTitle():MutableList<String> {
        allNameOfAvailablesClasses.clear()
        allListItems.forEach { label ->
            if (label.isSelected) {
                allNameOfAvailablesClasses.add(label.title)
            }
        }
        return allNameOfAvailablesClasses
    }
}
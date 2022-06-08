package com.example.classmanagerandroid.Screens.CreateCourse


import androidx.lifecycle.ViewModel
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.data.local.RolUser
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.auth
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.db
import com.example.classmanagerandroid.data.network.CourseImplement.Companion.createNewCourse
import com.example.classmanagerandroid.data.network.UsersImplement.Companion.updateUser
import com.example.classmanagerandroid.data.remote.Course
import com.example.classmanagerandroid.data.local.ListItem

class MainViewModelCreateCourse: ViewModel() {

    var allListItems = mutableListOf<ListItem>()
    val allNameOfAvailablesClasses =   mutableListOf<String>()

    fun createCourse(
        textNameOfCourse: String,
        textOfDescription: String,
        onFinished: () -> Unit
    ) {
        val mySelectedClasses = mutableListOf<String>()
        allListItems.forEach {
            if (it.isSelected)  mySelectedClasses.add(it.id)
        }

        val newCourse = Course(
            name = textNameOfCourse,
            description = textOfDescription,
            events = arrayListOf(),
            id = "",
            classes = mySelectedClasses,
            users = mutableListOf(
                RolUser(
                    id = "${auth.currentUser?.uid}",
                    rol = "admin"
                )
            ),
            img = "gs://class-manager-58dbf.appspot.com/appImages/books.jpg"
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
                                CurrentUser.updateDates(
                                    onFinished = {
                                        onFinished()
                                    }
                                )
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
            if (it.idOfCourse == "Sin asignar") {
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
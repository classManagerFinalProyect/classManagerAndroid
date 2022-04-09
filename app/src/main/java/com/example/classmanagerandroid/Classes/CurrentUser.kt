package com.example.classmanagerandroid.Classes

import com.example.classmanagerandroid.data.local.RolUser
import com.example.classmanagerandroid.data.remote.Course
import com.example.classmanagerandroid.data.remote.appUser
import com.example.classmanagerandroid.data.remote.Class
import com.example.classmanagerandroid.data.network.AccesToDataBase.Companion.db
import com.example.classmanagerandroid.data.network.AccesToDataBase.Companion.auth

class CurrentUser {
    companion object {
        val myCourses: MutableList<Course> = mutableListOf()
        val myClasses: MutableList<Class> = mutableListOf()
        var currentUser: appUser = appUser("","","", mutableListOf<String>(), arrayListOf<String>(),"","")



        //Varias consultas o una entera¿?
        fun getMyCourses() {
            myCourses.clear()
            currentUser.courses.forEach{ idOfCourse ->

                db.collection("course")
                    .document(idOfCourse)
                    .get()
                    .addOnSuccessListener {

                        myCourses.add(
                            Course(
                                name = it.get("name") as String,
                                classes = it.get("classes") as MutableList<String>,
                                users = it.get("users") as MutableList<RolUser>,
                                description = it.get("description") as String,
                                id = it.id
                            )
                        )
                    }
            }
        }

        fun getMyClasses() {
            myClasses.clear()
            currentUser.classes.forEach{ idOfClass ->

                db.collection("classes")
                    .document(idOfClass)
                    .get()
                    .addOnSuccessListener {


                        myClasses.add(
                            Class(
                                id = it.id,
                                name = it.get("name") as String,
                                description = it.get("description") as String,
                                idPractices = it.get("idPractices") as MutableList<String>,
                                users = it.get("users") as MutableList<RolUser>,
                                idOfCourse = it.get("idOfCourse") as String
                            )
                        )
                    }
            }
        }

        fun updateDates() {
            getMyClasses()
            getMyCourses()
        }

        fun uploadCurrentUser() {
            db.collection("users")
                .document(auth.currentUser?.uid.toString())
                .set(currentUser).addOnSuccessListener {
                    updateDates()
                }
        }
    }
}
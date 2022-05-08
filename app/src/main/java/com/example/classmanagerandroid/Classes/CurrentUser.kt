package com.example.classmanagerandroid.Classes

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.classmanagerandroid.data.local.RolUser
import com.example.classmanagerandroid.data.remote.Course
import com.example.classmanagerandroid.data.remote.appUser
import com.example.classmanagerandroid.data.remote.Class
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.db
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.auth
import com.google.firebase.storage.FirebaseStorage

class CurrentUser {
    companion object {
        val myCourses: MutableList<Course> = mutableListOf()
        val myClasses: MutableList<Class> = mutableListOf()
        var currentUser: appUser = appUser("","","", mutableListOf<String>(), arrayListOf<String>(),"","")
        var myImg = mutableStateOf<Uri?>(null)


        fun getCurrentImg(
            onFinished: () -> Unit
        ) {
            val storage = FirebaseStorage.getInstance()
            val gsReference = storage.getReferenceFromUrl(currentUser.imgPath)

            gsReference.downloadUrl.addOnSuccessListener{
                myImg.value = it
            }
            onFinished()
        }

        //Varias consultas o una entera¿?
        fun getMyCourses(
            onFinished:  () -> Unit
        ) {

            var firstAcces = true
            myCourses.clear()
            if(currentUser.courses.size == 0) onFinished()
            currentUser.courses.forEach{ idOfCourse ->

                db.collection("course")
                    .document(idOfCourse)
                    .get()
                    .addOnSuccessListener {
                        val users = it.get("users") as  MutableList<HashMap<String,String>>
                        val listOfRolUser: MutableList<RolUser> = mutableListOf()
                        users.forEach { rolUser ->
                            listOfRolUser.add(
                                RolUser(
                                    id = rolUser.get("id") as String,
                                    rol = rolUser.get("rol") as String
                                )
                            )
                        }



                        myCourses.add(
                            Course(
                                name = it.get("name") as String,
                                classes = it.get("classes") as MutableList<String>,
                                events = it.get("events") as MutableList<String>,
                                users = listOfRolUser,
                                description = it.get("description") as String,
                                id = it.id,
                                img = it.get("img") as String
                            )
                        )
                        if(firstAcces) {
                            firstAcces = false
                            onFinished()
                        }
                    }
            }
        }

        fun getMyClasses(
            onFinished:  () -> Unit
        ) {
            var firstAccess = true

            myClasses.clear()
            if(currentUser.classes.size == 0) onFinished()
            currentUser.classes.forEach{ idOfClass ->

                db.collection("classes")
                    .document(idOfClass)
                    .get()
                    .addOnSuccessListener {
                        val users = it.get("users") as  MutableList<HashMap<String,String>>
                        val listOfRolUser: MutableList<RolUser> = mutableListOf()
                        users.forEach { rolUser ->
                            listOfRolUser.add(
                                RolUser(
                                    id = rolUser.get("id") as String,
                                    rol = rolUser.get("rol") as String
                                )
                            )
                        }

                        myClasses.add(
                            Class(
                                id = it.id,
                                name = it.get("name") as String,
                                description = it.get("description") as String,
                                idPractices = it.get("idPractices") as MutableList<String>,
                                users = listOfRolUser,
                                idOfCourse = it.get("idOfCourse") as String,
                                img = it.get("img") as String
                            )
                        )
                        if(firstAccess) {
                            firstAccess = false
                            onFinished()
                        }
                    }
            }
        }

        fun updateDates(
            onFinished:  () -> Unit
        ) {
            getCurrentImg(onFinished = {})
            getMyClasses(
                onFinished = {
                    getMyCourses(
                        onFinished = {
                            onFinished()
                        }
                    )
                }
            )
        }

        fun uploadCurrentUser(
            onFinished: () -> Unit
        ) {
            db.collection("users")
                .document(auth.currentUser?.uid.toString())
                .set(currentUser).addOnSuccessListener {
                    updateDates(
                        onFinished = {
                            onFinished()
                        }
                    )
                }
        }
    }
}
package com.example.classmanagerandroid.data.local

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import com.example.classmanagerandroid.data.remote.Course
import com.example.classmanagerandroid.data.remote.AppUser
import com.example.classmanagerandroid.data.remote.Class
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.db
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.auth
import com.google.firebase.storage.FirebaseStorage

class CurrentUser {
    companion object {
        val myCourses: MutableList<Course> = mutableListOf()
        val myClasses: MutableList<Class> = mutableListOf()
        var currentUser: AppUser = AppUser("","","", mutableListOf<String>(), arrayListOf<String>(),"","","")
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

        fun getMyCourses(
            onFinished:  () -> Unit
        ) {
            var count = 0
            myCourses.clear()
            if(currentUser.courses.size == 0) onFinished()
            currentUser.courses.forEach{ idOfCourse ->

                db.collection("course")
                    .document(idOfCourse)
                    .get()
                    .addOnCompleteListener{
                        if(it.isSuccessful) {
                            val users = it.result.get("users") as  MutableList<HashMap<String,String>>
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
                                    name = it.result.get("name") as String,
                                    classes = it.result.get("classes") as MutableList<String>,
                                    events = it.result.get("events") as MutableList<String>,
                                    users = listOfRolUser,
                                    description = it.result.get("description") as String,
                                    id = it.result.id,
                                    img = it.result.get("img") as String
                                )
                            )
                            count++
                        }
                        else {
                            count++
                        }

                        if(count == currentUser.courses.size) onFinished()
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
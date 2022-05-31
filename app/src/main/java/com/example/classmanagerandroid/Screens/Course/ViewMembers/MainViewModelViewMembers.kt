package com.example.classmanagerandroid.Screens.Course.ViewMembers

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchBar.SearchWidgetState
import com.example.classmanagerandroid.data.local.RolUser
import com.example.classmanagerandroid.data.network.AccessToDataBase
import com.example.classmanagerandroid.data.network.CourseImplement.Companion.updateCourse
import com.example.classmanagerandroid.data.network.UsersImplement.Companion.getUserById
import com.example.classmanagerandroid.data.remote.Course
import com.example.classmanagerandroid.data.remote.AppUser

class MainViewModelViewMembers: ViewModel() {
    var selectedCourse: Course = Course(arrayListOf(), arrayListOf(), arrayListOf(),"","","","")
    var selectedusers: MutableList<AppUser> = arrayListOf()
    var currentdRolUser: RolUser = RolUser("","")

    fun getSelectedCourse(
        idCourse: String,
    ) {
        AccessToDataBase.db.collection("course")
            .document(idCourse)
            .get()
            .addOnSuccessListener {
                val users = it.get("users") as  MutableList<HashMap<String,String>> //Any
                val listOfRolUser: MutableList<RolUser> = mutableListOf()
                users.forEach {
                    listOfRolUser.add(
                        RolUser(
                            id = it.get("id") as String,
                            rol = it.get("rol") as String
                        )
                    )
                }

                selectedCourse =
                    Course(
                        name = it.get("name") as String,
                        classes = it.get("classes") as MutableList<String>,
                        events = it.get("events") as MutableList<String>,
                        users = listOfRolUser,
                        description = it.get("description") as String,
                        id = it.id,
                        img = it.get("img") as String
                    )
                getAllUser(selectedCourse.users)
                getCurrentRolUser()
            }
    }

    fun getCurrentRolUser() {
        selectedCourse.users.forEach{
            if(it.id.equals(CurrentUser.currentUser.id))
                currentdRolUser = it
        }
    }

    fun getAllUser(
        allUsers: MutableList<RolUser>
    ){
        selectedusers.clear()
        allUsers.forEach{
            getUserById(
                idOfUser = it.id,
                onFinished = { finish, user ->
                    if (finish) {
                        selectedusers.add(user)
                    }
                }
            )
        }
    }
    fun getRolOfUserById(
        idOfuser: String
    ) : RolUser{
        selectedCourse.users.forEach {
            if(it.id.equals(idOfuser)) {
                return it
            }
        }
        return RolUser("","")
    }

    fun deleteRolUser(
        selectedUser: AppUser,
        onFinish: () -> Unit
    ) {
        var deleteuser = RolUser("","")
        selectedCourse.users.forEach {
            if(it.id.equals(selectedUser.id)) {
                deleteuser = it
                selectedusers.remove(selectedUser)
                return@forEach
            }
        }
        selectedCourse.users.remove(deleteuser)

        updateCourse(
            newCourse = selectedCourse,
            onFinished = { finish, course ->
                if (finish) {
                    Log.d("Delete rol user course","delete rol user")
                }
                onFinish()
            }
        )
    }

    fun updateRol(
        idOfuser: String,
        newRol: String,
        onFinish: () -> Unit
    ) {
        selectedCourse.users.forEach {
            if(it.id.equals(idOfuser)) {
                it.rol = newRol
                updateCourse(
                    newCourse = selectedCourse,
                    onFinished = { finish, course ->
                        if (finish) {
                            Log.d("Update course","update course")
                        }
                        onFinish()
                    }
                )
            }
        }
    }

    //Search Bar
    private val _searchWidgetState: MutableState<SearchWidgetState> = mutableStateOf(value = SearchWidgetState.CLOSED)
    val searchWidgetState: State<SearchWidgetState> = _searchWidgetState

    private val _searchTextState: MutableState<String> = mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        _searchWidgetState.value = newValue
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }

    fun clearSearchBar() {
        _searchTextState.value = ""
        _searchWidgetState.value = SearchWidgetState.CLOSED
    }

}
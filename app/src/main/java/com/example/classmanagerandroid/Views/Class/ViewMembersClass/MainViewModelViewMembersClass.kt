package com.example.classmanagerandroid.Views.Class.ViewMembersClass

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.classmanagerandroid.Classes.CurrentUser
import com.example.classmanagerandroid.Views.Course.ViewMembers.SearchWidgetState
import com.example.classmanagerandroid.data.local.RolUser
import com.example.classmanagerandroid.data.network.AccesToDataBase
import com.example.classmanagerandroid.data.network.ClassesImplement.Companion.getClassById
import com.example.classmanagerandroid.data.network.ClassesImplement.Companion.updateClass
import com.example.classmanagerandroid.data.network.CourseImplement
import com.example.classmanagerandroid.data.network.UsersImplement
import com.example.classmanagerandroid.data.remote.Course
import com.example.classmanagerandroid.data.remote.Class
import com.example.classmanagerandroid.data.remote.appUser

class MainViewModelViewMembersClass: ViewModel() {
    var selectedClass = Class("","","", arrayListOf(), arrayListOf(),"")
    var selectedUsers: MutableList<appUser> = arrayListOf()
    var currentdRolUser: RolUser = RolUser("","")

    fun getSelectedClass(
        idOfClass: String,
    ) {
        getClassById(
            idOfClass = idOfClass,
            onFinished = { finish, Class ->
                if(finish) {
                    selectedClass = Class
                    getAllUser(selectedClass.users)
                    getCurrentRolUser()
                }
                else {
                    Log.d("ERROR: getselectedClass","class not found")
                }
            }
        )
    }

    fun getCurrentRolUser() {
        selectedClass.users.forEach{
            if(it.id.equals(CurrentUser.currentUser.id))
                currentdRolUser = it
        }
    }

    fun getAllUser(
        allUsers: MutableList<RolUser>
    ){
        selectedUsers.clear()
        allUsers.forEach{
            UsersImplement.getUserById(
                idOfUser = it.id,
                onFinished = { finish, user ->
                    if (finish) {
                        selectedUsers.add(user)
                    }
                }
            )
        }
    }

    fun getRolOfUserById(
        idOfuser: String
    ) : RolUser{
        selectedClass.users.forEach {
            if(it.id.equals(idOfuser)) {
                return it
            }
        }
        return RolUser("","")
    }

    fun deleteRolUser(
        selectedUser: appUser,
        onFinish: () -> Unit
    ) {
        var deleteuser = RolUser("","")
        selectedClass.users.forEach {
            if(it.id.equals(selectedUser.id)) {
                deleteuser = it
                selectedUsers.remove(selectedUser)
                return@forEach
            }
        }
        selectedClass.users.remove(deleteuser)

        updateClass(
            newClass = selectedClass,
            onFinished = { finish, course ->
                if (finish) {
                    Log.d("Delete rol user", "delete rol user in a class")
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
        selectedClass.users.forEach {
            if(it.id.equals(idOfuser)) {
                it.rol = newRol
                updateClass(
                    newClass = selectedClass,
                    onFinished = { finish, course ->
                        if (finish) {
                            Log.d("Update course", "update course")
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
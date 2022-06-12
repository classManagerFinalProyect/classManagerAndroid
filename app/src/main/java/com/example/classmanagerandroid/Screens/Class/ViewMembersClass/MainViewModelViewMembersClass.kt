package com.example.classmanagerandroid.Screens.Class.ViewMembersClass

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchBar.SearchWidgetState
import com.example.classmanagerandroid.data.local.RolUser
import com.example.classmanagerandroid.data.network.ClassesImplement.Companion.getClassById
import com.example.classmanagerandroid.data.network.ClassesImplement.Companion.updateClass
import com.example.classmanagerandroid.data.network.UsersImplement
import com.example.classmanagerandroid.data.remote.Class
import com.example.classmanagerandroid.data.remote.AppUser

class MainViewModelViewMembersClass: ViewModel() {
    var selectedClass = Class("","","", arrayListOf(), arrayListOf(),"","")
    var selectedUsers: MutableList<AppUser> = arrayListOf()
    var currentRolUser: RolUser = RolUser("","")

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
                    Log.d("ERROR: getSelectedClass","class not found")
                }
            }
        )
    }

    private fun getCurrentRolUser() {
        selectedClass.users.forEach{
            if(it.id == CurrentUser.currentUser.id)
                currentRolUser = it
        }
    }

    private fun getAllUser(
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
        idUser: String
    ) : RolUser{
        selectedClass.users.forEach {
            if(it.id == idUser) {
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
        selectedClass.users.forEach {
            if(it.id == selectedUser.id) {
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
        idUser: String,
        newRol: String,
        onFinish: () -> Unit
    ) {
        selectedClass.users.forEach {
            if(it.id == idUser) {
                it.rol = newRol
                updateClass(
                    newClass = selectedClass,
                    onFinished = { finish, _ ->
                        if (finish) {
                            Log.d("Update class", "update class")
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
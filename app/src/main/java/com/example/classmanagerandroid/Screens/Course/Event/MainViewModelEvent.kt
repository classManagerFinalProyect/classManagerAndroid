package com.example.classmanagerandroid.Screens.Course.Event

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchBar.SearchWidgetState
import com.example.classmanagerandroid.data.local.RolUser
import com.example.classmanagerandroid.data.network.AccessToDataBase
import com.example.classmanagerandroid.data.network.ClassesImplement.Companion.getClassById
import com.example.classmanagerandroid.data.network.CourseImplement.Companion.updateCourse
import com.example.classmanagerandroid.data.network.EventImplement.Companion.deleteEventById
import com.example.classmanagerandroid.data.network.EventImplement.Companion.getEventById
import com.example.classmanagerandroid.data.network.EventImplement.Companion.updateEvent
import com.example.classmanagerandroid.data.network.EventImplement.Companion.uploadEvent
import com.example.classmanagerandroid.data.remote.Class
import com.example.classmanagerandroid.data.remote.Course
import com.example.classmanagerandroid.data.remote.Event


class MainViewModelEvent: ViewModel() {
    var selectedCourse: Course = Course(arrayListOf(), arrayListOf(), arrayListOf(),"","","","")
    var rolOfSelectedUserInCurrentCourse: RolUser = RolUser(id = "", rol = "Sin asignar")
    val selectedEvents: MutableList<Event> = mutableListOf()
    val selectedClasses: MutableList<Class> = mutableListOf()

    fun clearVariables() {
        selectedEvents.clear()
        selectedClasses.clear()
    }

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

                getSelectedEvents(selectedCourse.events)
                getSelectedClasses(selectedCourse.classes)
                getRolOfUser(CurrentUser.currentUser.id)
            }
    }

    private fun getSelectedClasses(
        classes: MutableList<String>
    ) {
        selectedEvents.clear()
        selectedClasses.clear()
        classes.forEach{
            getClassById(
                idOfClass = it,
                onFinished = { finish, item ->
                    if (finish) {
                        selectedClasses.add(item)
                    }
                }
            )
        }
    }

    fun getRolOfUser(
        idOfUser: String
    ) {
        selectedCourse.users.forEach {
            if (it.id.equals(idOfUser)) rolOfSelectedUserInCurrentCourse = it
        }
    }


    fun getSelectedEvents(
        allEventIds: MutableList<String>,
    ) {
        selectedEvents.clear()
        allEventIds.forEach {
            getEventById(
                idOfEvent = it,
                onFinished = { finish, event ->
                    if (finish)
                        selectedEvents.add(event)
                    else
                        Log.d("Error","Error to get Event")
                }
            )
        }
    }



    fun createNewEvent(
        nameOfEvent: String,
        date: String,
        initialTime: String,
        finalTime: String,
        nameOfClass: String,
        onFinished: () -> Unit
    ) {

        uploadEvent(
            newEvent = Event(
                id = "",
                name = nameOfEvent,
                idOfCourse = selectedCourse.id,
                initialTime = initialTime,
                finalTime =  finalTime,
                nameOfClass = nameOfClass,
                date = date
            ),
            onFinished =  { finish , newEvent ->
                if(finish) {
                    selectedCourse.events.add(newEvent.id)
                    updateCourse(
                        newCourse = selectedCourse,
                        onFinished = { finish , course ->
                            if(finish) {
                                selectedEvents.add(newEvent)
                                onFinished()
                            }
                        }
                    )
                }
            }
        )

    }

    fun updateEvents(
        idOfEvent: String,
        event: Event,
        context: Context
    ) {

        updateEvent(
            idOfEvent = idOfEvent,
            event = event,
            onFinished = {
                if (it) {
                    Toast.makeText(context,"El evento se ha actualizado con exito",Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    fun deleteAllEvents(
        onFinished: () -> Unit
    ) {
        var count = 0
        selectedCourse.events.forEach {
            deleteEvent(
                idOfEvent = it,
                onFinished = {
                    count++
                    if(selectedCourse.events.size == 1) {
                        selectedCourse.events.clear()
                        selectedEvents.clear()
                        onFinished()
                    }
                }
            )
        }

    }

    fun deleteEvent(
        idOfEvent: String,
        onFinished: () -> Unit
    ) {
        deleteEventById(
            idOfEvent = idOfEvent,
            onFinished = {
                onFinished()
                if (it) {
                    selectedCourse.events.remove(idOfEvent)
                    updateCourse(
                        newCourse = selectedCourse,
                        onFinished = { finis, item ->
                            if(finis) {
                                Log.d("Finish delete","El evento se ha eliminado correctamente")
                            }
                        }
                    )
                }
            }
        )

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
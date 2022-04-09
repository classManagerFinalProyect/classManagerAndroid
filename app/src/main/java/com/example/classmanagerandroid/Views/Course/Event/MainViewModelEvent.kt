package com.example.classmanagerandroid.Views.Course.Event

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.classmanagerandroid.data.network.EventImplement.Companion.uploadEvent
import com.example.classmanagerandroid.data.remote.Event


class MainViewModelEvent: ViewModel() {

    fun createNewEvent(
        nameOfEvent: String,
        initialDate: String,
        finalDate: String,
        nameOfClass: String
    ) {

        uploadEvent(
            newEvent = Event(
                id = "",
                name = nameOfEvent,
                idOfCourse = "",
                initalDate = initialDate,
                finalDate =  finalDate,
                nameOfClass = nameOfClass
            ),
            onFinished =  { finish , newEvent ->
                if(finish) {

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
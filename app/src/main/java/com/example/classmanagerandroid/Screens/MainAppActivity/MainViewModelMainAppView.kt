package me.saine.android.Views.MainAppActivity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.classmanagerandroid.Screens.MainAppActivity.Components.MainBody.ContentState
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchBar.SearchWidgetState
import com.example.classmanagerandroid.data.local.Message
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.auth
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.db
import com.example.classmanagerandroid.data.remote.Chat
import com.example.classmanagerandroid.data.remote.Course
import com.example.classmanagerandroid.data.remote.Practice

class MainViewModelMainAppView: ViewModel() {


    lateinit var chat: Chat
    lateinit var selectedPractice : Practice


    var myCourses = mutableListOf<Course>()
    val allCourses = mutableListOf<Course>()


    fun getMyCourses() {
        myCourses.clear()
        allCourses.forEach{
            it.users.forEach{ admins ->
                if (admins.equals(auth.currentUser?.uid.toString())) {
                    myCourses.add(it)
                }
            }
        }
    }
    fun getChatsOfClass(idPractice: String) {

        db.collection("practices")
            .document(idPractice)
            .get()
            .addOnSuccessListener {
                selectedPractice =
                    Practice(
                        id = it.id,
                        name = it.get("name") as String,
                        teacherAnnotation =  it.get("teacherAnnotation") as String,
                        idOfChat = it.get("idOfChat") as String,
                        description = it.get("description") as String,
                        idOfClass = it.get("idOfClass") as String,
                        deliveryDate = it.get("deliveryDate") as String
                    )
                getChat(selectedPractice.idOfChat)
            }
    }

    fun getChat(
        idChat: String
    ) {

        db.collection("practicesChats")
            .document(idChat)
            .get()
            .addOnSuccessListener {
                chat =
                    Chat(
                        id = it.id,
                        conversation = it.get("conversation") as MutableList<Message>
                    )
            }
    }


    //Content State
    private val _contentState: MutableState<ContentState> = mutableStateOf(value = ContentState.ALL)
    val contentState: State<ContentState> = _contentState


    fun updateContentState(newValue: ContentState) {
        _contentState.value = newValue
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
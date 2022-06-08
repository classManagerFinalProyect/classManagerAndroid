package com.example.classmanagerandroid.Screens.Class

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchBar.SearchWidgetState
import com.example.classmanagerandroid.data.local.Message
import com.example.classmanagerandroid.data.local.RolUser
import com.example.classmanagerandroid.data.network.AccessToDataBase
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.db
import com.example.classmanagerandroid.data.network.ChatImplement.Companion.deleteChatById
import com.example.classmanagerandroid.data.network.ClassesImplement.Companion.updateClass
import com.example.classmanagerandroid.data.network.PracticeImplement.Companion.deletePracticeById
import com.example.classmanagerandroid.data.network.UsersImplement.Companion.updateUser
import com.example.classmanagerandroid.data.remote.Chat
import com.example.classmanagerandroid.data.remote.AppUser
import com.example.classmanagerandroid.data.remote.Class
import com.example.classmanagerandroid.data.remote.Practice
import com.google.android.gms.tasks.Task


class MainViewModelClass:ViewModel() {

    var selectedClass: Class = Class("","","", arrayListOf(), arrayListOf(),"","")
    var selectedPractices = arrayListOf<Practice>()
    var rolOfSelectedUserInCurrentClass: RolUser = RolUser(id = "", rol = "Sin asignar")
    private lateinit var addNewUser: AppUser
    val classImg =  mutableStateOf<Uri?>(null)

    fun clearVariables() {
        selectedClass= Class("","","", arrayListOf(), arrayListOf(),"","")
        selectedPractices = arrayListOf()
        rolOfSelectedUserInCurrentClass = RolUser(id = "", rol = "Sin asignar")
        addNewUser = AppUser("","","", arrayListOf(), arrayListOf(),"","","")
        classImg.value = null
    }

    private fun getClassImg() {
        val gsReference = AccessToDataBase.storageInstance.getReferenceFromUrl(selectedClass.img)
        gsReference.downloadUrl.addOnSuccessListener { classImg.value = it }
    }

    fun getSelectedClass(
        idClass: String,
        onValueFinish: () -> Unit
    ) {
        db.collection("classes")
            .document(idClass)
            .get()
            .addOnSuccessListener {
                val users = it.get("users") as  MutableList<HashMap<String,String>>
                val listOfRolUser: MutableList<RolUser> = mutableListOf()
                users.forEach { user ->
                    listOfRolUser.add(
                        RolUser(
                            id = user.get("id") as String,
                            rol = user.get("rol") as String
                        )
                    )
                }

                selectedClass =
                   Class(
                       id = it.id,
                       idPractices = it.get("idPractices") as MutableList<String>,
                       users = listOfRolUser,
                       name = it.get("name") as String,
                       description = it.get("description") as String,
                       idOfCourse = it.get("idOfCourse") as String,
                       img = it.get("img") as String
                   )
                getRolOfClass(CurrentUser.currentUser.id)
                getAllPractices(selectedClass.idPractices)
                getClassImg()
                onValueFinish()
            }
    }

    private fun getAllPractices(
        idPractices: MutableList<String>
    ) {
        selectedPractices.clear()
        idPractices.forEach{ idOfPractice ->
            db.collection("practices")
                .document(idOfPractice)
                .get()
                .addOnSuccessListener {
                    selectedPractices.add(
                        Practice(
                            description = it.get("description") as String,
                            id = it.get("id") as String,
                            name = it.get("name") as String,
                            idOfChat = it.get("idOfChat") as String,
                            teacherAnnotation = it.get("teacherAnnotation") as String,
                            idOfClass = it.get("idOfClass") as String,
                            deliveryDate = it.get("deliveryDate") as String
                        )
                    )
                }
        }
    }

    fun createNewPractice(
        practice: Practice,
        navController: NavController
    ) {

        createPracticeChat { idOfChat ->

            val document = db.collection("practices").document()
            val idOfDocument = document.id

            practice.idOfChat = idOfChat
            practice.id = document.id

            document
                .set(practice)
                .addOnSuccessListener {

                selectedClass.idPractices.add(idOfDocument)
                db.collection("classes")
                    .document(selectedClass.id)
                    .set(selectedClass).addOnSuccessListener {
                        CurrentUser.updateDates(
                            onFinished = {
                                navController.navigate("${Destinations.Practice.route}/${idOfDocument}")
                            }
                        )
                    }
            }
        }

    }

    private fun createPracticeChat(onValueFinish: (String) -> Task<Void>) {
        val document = db.collection("practicesChats").document()
        val idOfDocument = document.id

        document.set(
            hashMapOf(
                "conversation" to  mutableListOf<Message>(),
                "id" to idOfDocument
            )
            ).addOnSuccessListener{
                onValueFinish(idOfDocument)
            }
    }

    fun deleteClass(
        context: Context,
        navController: NavController
    ) {
        db.collection("classes")
            .document(selectedClass.id)
            .delete()
            .addOnSuccessListener {
                selectedPractices.forEach{
                    deletePracticeById(
                        idOfPractice = it.id,
                        onFinished = {}
                    )
                    deleteChatById(
                        idOfChat = it.idOfChat,
                        onFinished = {}
                    )
                }


                CurrentUser.myCourses.forEach{
                    if(it.id == selectedClass.idOfCourse) {
                        it.classes.remove(selectedClass.id)
                        deleteIdOfCourse(it.classes)
                    }
                }
                CurrentUser.myClasses.remove(selectedClass)
                CurrentUser.currentUser.classes.remove(selectedClass.id)

                selectedClass.users.forEach {
                    deleteIfOfClassByUserId(it.id)
                }

                selectedPractices.clear()

                CurrentUser.uploadCurrentUser(
                    onFinished = {
                        navController.popBackStack()
                    }
                )
                Toast.makeText(context,"La clase se ha eliminado correctamente", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteIfOfClassByUserId(
        idOfUser: String
    ) {
        db.collection("users")
            .document(idOfUser)
            .get()
            .addOnSuccessListener {

                if (it.exists()) {

                    val entity = AppUser(
                        id = it.get("id") as String,
                        classes = it.get("classes") as MutableList<String>,
                        description = it.get("description") as String,
                        name =  it.get("name") as String,
                        courses = it.get("courses") as MutableList<String>,
                        imgPath = it.get("imgPath") as String,
                        email = it.get("email") as String,
                        password = it.get("password") as String
                    )
                    entity.classes.remove(selectedClass.id)
                    updateUser(
                        idOfUser = entity.id,
                        user = entity,
                        onFinished = {}
                    )
                }

            }

    }

    private fun deleteIdOfCourse(
        newValueOfClasses: MutableList<String>
    ) {
        db.collection("course")
            .document(selectedClass.idOfCourse)
            .update("classes",newValueOfClasses)
    }

    fun addNewUser(
        idOfUser: String,
        context: Context,
        textSelectedItem: String
    ) {
        checkIfUserExist (
            idOfUser = idOfUser,
            onFinishResult = {
                if (it) {
                    if (!checkIfUserIsInscribedInClass(idOfUser)) {
                        selectedClass.users.add(
                            RolUser(
                                id = idOfUser,
                                rol = textSelectedItem
                            )
                        )
                        addNewUser.classes.add(selectedClass.id)
                        db.collection("users")
                            .document(idOfUser)
                            .set(addNewUser)
                            .addOnSuccessListener {
                                db.collection("classes")
                                    .document(selectedClass.id)
                                    .set(selectedClass)
                                    .addOnSuccessListener {
                                        CurrentUser.updateDates(
                                            onFinished = {
                                                Toast.makeText(context,"El usuario se ha agregado correctamente", Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                            }
                    }
                    else
                        Toast.makeText(context,"El usuario ya participa en esta clase",Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(context,"La id del usuario no existe", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun checkIfUserIsInscribedInClass(
        idOfUser: String
    ):Boolean {
        selectedClass.users.forEach {
            if (it.id == idOfUser)
                return true
        }
        return false
    }

    private fun checkIfUserExist(
        idOfUser: String,
        onFinishResult: (Boolean) -> Unit
    ) {
        db.collection("users")
            .document(idOfUser)
            .get()
            .addOnCompleteListener {

                if (it.result.exists()) {
                    addNewUser = AppUser(
                        id = it.result.get("id") as String,
                        classes = it.result.get("classes") as MutableList<String>,
                        description = it.result.get("description") as String,
                        name =  it.result.get("name") as String,
                        courses = it.result.get("courses") as MutableList<String>,
                        imgPath = it.result.get("imgPath") as String,
                        email = it.result.get("email") as String,
                        password = it.result.get("password") as String
                    )
                    onFinishResult(true)
                }
                else
                    onFinishResult(false)
            }
    }

    private fun getRolOfClass(
        idOfUser: String
    ) {
        selectedClass.users.forEach {
            if (it.id == idOfUser){
                rolOfSelectedUserInCurrentClass = it
            }
        }
    }

    fun updateCurrentClass(
        updateClass: Class,
        onFinishResult: () -> Unit
    ){
        updateClass(
            newClass = updateClass,
            onFinished = { b: Boolean, updateClass: Class ->
                CurrentUser.getMyClasses( onFinished = {} )
                onFinishResult()
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
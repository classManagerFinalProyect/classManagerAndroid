package com.example.classmanagerandroid.Screens.Course

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.data.local.CurrentUser.Companion.currentUser
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.SearchBar.SearchWidgetState
import com.example.classmanagerandroid.data.local.RolUser
import com.example.classmanagerandroid.data.network.AccessToDataBase
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.auth
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.db
import com.example.classmanagerandroid.data.network.ClassesImplement.Companion.updateClass
import com.example.classmanagerandroid.data.network.CourseImplement.Companion.updateCourse
import com.example.classmanagerandroid.data.network.EventImplement.Companion.deleteEventById
import com.example.classmanagerandroid.data.network.UsersImplement
import com.example.classmanagerandroid.data.network.UsersImplement.Companion.getUserById
import com.example.classmanagerandroid.data.remote.Course
import com.example.classmanagerandroid.data.remote.AppUser
import com.example.classmanagerandroid.data.remote.Class

class MainViewModelCourse: ViewModel() {

    var selectedCourse: Course = Course(arrayListOf(), arrayListOf(), arrayListOf(),"","","","")
    var selectedClasses: MutableList<Class> = mutableListOf()
    private lateinit var addNewUser: AppUser
    var rolOfSelectedUserInCurrentCourse: RolUser = RolUser(id = "", rol = "Sin asignar")
    val courseImg =  mutableStateOf<Uri?>(null)

    fun leaveCourse(
        onFinishResult: () -> Unit
    ) {
        selectedClasses.forEach {
            it.users.remove(rolOfSelectedUserInCurrentCourse)
            currentUser.classes.remove(it.id)

            updateClass(
                newClass = it,
                onFinished = { b , updateClass ->
                    selectedCourse.users.remove(rolOfSelectedUserInCurrentCourse)
                    currentUser.courses.remove(selectedCourse.id)
                    updateCurrentCourse(
                        updateCourse = selectedCourse,
                        onFinishResult = {
                                CurrentUser.uploadCurrentUser(onFinished = {
                                    onFinishResult()
                                }
                            )
                        }
                    )
                }
            )
        }
    }

    private fun getCourseImg() {
        val gsReference = AccessToDataBase.storageInstance.getReferenceFromUrl(selectedCourse.img)
        gsReference.downloadUrl.addOnSuccessListener { courseImg.value = it }
    }

    fun clearVariables() {
         selectedCourse = Course(arrayListOf(), arrayListOf(), arrayListOf(),"","","","")
         selectedClasses = mutableListOf()
         addNewUser = AppUser("","","", arrayListOf(), arrayListOf(),"","","")
         rolOfSelectedUserInCurrentCourse =  RolUser(id = "", rol = "Sin asignar")
    }

    fun getSelectedCourse(
        idCourse: String,
        onFinishResult: () -> Unit
    ) {
        db.collection("course")
            .document(idCourse)
            .get()
            .addOnCompleteListener {
                if (it.result.exists()) {
                    val users = it.result.get("users") as  MutableList<HashMap<String,String>> //Any
                    val listOfRolUser: MutableList<RolUser> = mutableListOf()
                    users.forEach { task ->
                        listOfRolUser.add(
                            RolUser(
                                id = task.get("id") as String,
                                rol = task.get("rol") as String
                            )
                        )
                    }

                    selectedCourse =
                        Course(
                            name = it.result.get("name") as String,
                            classes = it.result.get("classes") as MutableList<String>,
                            events = it.result.get("events") as MutableList<String>,
                            users = listOfRolUser,
                            description = it.result.get("description") as String,
                            id = it.result.id,
                            img = it.result.get("img") as String

                        )
                    getRolOfUser(currentUser.id)
                    getCourseImg()

                    getSelectedClasses(
                        allSelectedClasses = selectedCourse.classes,
                        onFinishResult = {
                            onFinishResult()
                        }
                    )
                }
                else {
                    onFinishResult()
                }
            }
    }



    private fun getSelectedClasses(
        allSelectedClasses: MutableList<String>,
        onFinishResult: () -> Unit
    ) {
        selectedClasses.clear()
        var count = 0
        if(allSelectedClasses.size == 0) onFinishResult()
        allSelectedClasses.forEach{ idOfCourse ->

            db.collection("classes")
                .document(idOfCourse)
                .get()
                .addOnCompleteListener {
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

                        selectedClasses.add(
                            Class(
                                name = it.result.get("name") as String,
                                idPractices = it.result.get("idPractices") as MutableList<String>,
                                users = listOfRolUser,
                                description = it.result.get("description") as String,
                                id = it.result.id,
                                idOfCourse = it.result.get("idOfCourse") as String,
                                img = it.result.get("img") as String
                            )
                        )
                        count++
                    }
                    else {
                        count++
                    }
                    if (count == allSelectedClasses.size) onFinishResult()
                }
        }
    }

    fun deleteCurse(
        context: Context,
        navController: NavController
    ) {
        db.collection("course")
            .document(selectedCourse.id)
            .delete()
            .addOnSuccessListener {
                selectedClasses.forEach{
                    currentUser.classes.remove(it.id)
                    CurrentUser.myClasses.remove(it)
                    deleteClass(it.id)
                }
                currentUser.courses.remove(selectedCourse.id)
                selectedCourse.users.forEach {
                    deleteIfOfCourseByUserId(it.id)
                }
                selectedCourse.events.forEach{
                    deleteEventById(
                        idOfEvent = it,
                        onFinished = {}
                    )
                }
                CurrentUser.myCourses.remove(selectedCourse)
                selectedCourse = Course(arrayListOf(), arrayListOf(), arrayListOf(),"","","","")
                selectedClasses.clear()

                CurrentUser.uploadCurrentUser(
                    onFinished = {
                        navController.popBackStack()
                    }
                )
                Toast.makeText(context,"El curso se ha eliminado correctamente",Toast.LENGTH_SHORT).show()
            }
    }
    private fun deleteIfOfCourseByUserId(
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
                    entity.classes.remove(selectedCourse.id)
                    uploadUser(entity)
                }
            }
    }

    private fun uploadUser(
        AppUser: AppUser
    ) {
        db.collection("users")
            .document(AppUser.id)
            .set(AppUser)
    }

    private fun deleteClass(idOfClass: String) {
        db.collection("classes")
            .document(idOfClass)
            .delete()
            .addOnSuccessListener {
            }
    }

    private fun updateUserById(
        id: String,
        newClassId: String
    ) {
        getUserById(
            idOfUser = id,
            onFinished = { b, appUser ->
                appUser.classes.add(newClassId)

                if(b) {
                    UsersImplement.updateUser(
                        idOfUser = id,
                        user = appUser,
                        onFinished = {

                        }
                    )
                }
            }
        )
    }

    fun createNewClass(
        navController: NavController,
        context: Context,
        textDescription: String,
        textNameOfClass: String,
        itemSelectedCurse: Course
    ) {
        val document = db.collection("classes").document()
        val idOfDocument = document.id
        val allRolUser: MutableList<RolUser> = arrayListOf()

        selectedCourse.users.forEach {
            allRolUser.add(it)
            updateUserById(
                it.id,
                idOfDocument
            )
        }


        allRolUser.add(
            RolUser(
                id = "${auth.currentUser?.uid}",
                rol = "admin"
            )
        )

        val newClass = Class(
            name = textNameOfClass,
            id = document.id,
            idOfCourse = itemSelectedCurse.id,
            description = textDescription,
            idPractices = mutableListOf(),
            users = allRolUser,
            img = selectedCourse.img
        )

        document
            .set(newClass)
            .addOnSuccessListener {
            if(itemSelectedCurse.name != "Sin Asignar") {
                itemSelectedCurse.classes.add(idOfDocument)
                db.collection("course")
                    .document(itemSelectedCurse.id)
                    .set(itemSelectedCurse)
            }

            currentUser.classes.add(idOfDocument)

            db.collection("users")
                .document(auth.currentUser?.uid.toString())
                .set(currentUser)
                .addOnSuccessListener {
                    CurrentUser.updateDates(
                        onFinished = {
                            Toast.makeText(context,"La clase ha sido creado correctamente", Toast.LENGTH_SHORT).show()
                            navController.navigate("${Destinations.Class.route}/${idOfDocument}")
                        }
                    )
                }
        }
    }
    fun checkIfUserExist (
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

    private fun checkIfUserIsInscribedInCourse(
        idOfUser: String
    ):Boolean {
        selectedCourse.users.forEach {
            if (it.id.equals(idOfUser))
                return true
        }
        return false
    }

    fun addNewUser(
        idOfUser: String,
        context: Context,
        textSelectedItem: String
    ) {
        checkIfUserExist(
            idOfUser = idOfUser,
            onFinishResult = {
                if (it) {
                    if (!checkIfUserIsInscribedInCourse(idOfUser)) {
                        val newRolUser = RolUser(
                            id = idOfUser,
                            rol = textSelectedItem
                        )

                        selectedCourse.users.add(newRolUser)
                        addNewUser.courses.add(selectedCourse.id)

                        selectedClasses.forEach{ newClass ->
                            addNewUser.classes.add(newClass.id)
                        }

                        addNewMemberInCLasses(
                            rolUser = newRolUser,
                            onFinished = {

                            }
                        )
                        db.collection("users")
                            .document(idOfUser)
                            .set(addNewUser)
                            .addOnSuccessListener {
                                db.collection("course")
                                    .document(selectedCourse.id)
                                    .set(selectedCourse)
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
                        Toast.makeText(context,"El usuario ya participa en este curso",Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(context,"La id del usuario no existe", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun addNewMemberInCLasses(
        onFinished: () -> Unit,
        rolUser: RolUser
    ){
        selectedClasses.forEach{
            it.users.add(rolUser)

            updateClass(
                newClass = it,
                onFinished = { b, classes ->
                    onFinished()
                }
            )
        }

    }

    fun updateCurrentCourse(
        updateCourse: Course,
        onFinishResult: () -> Unit
    ) {
        updateCourse(
            newCourse = updateCourse,
            onFinished = { _: Boolean, _: Course ->
                CurrentUser.getMyCourses( onFinished = {} )
                onFinishResult()
            }
        )
    }
    private fun getRolOfUser(
        idOfUser: String
    ) {
        selectedCourse.users.forEach {
            if (it.id == idOfUser) rolOfSelectedUserInCurrentCourse = it
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
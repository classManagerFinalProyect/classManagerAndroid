package com.example.classmanagerandroid.Screens.Practice

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.classmanagerandroid.data.local.CurrentUser
import com.example.classmanagerandroid.data.local.Message
import com.example.classmanagerandroid.data.local.RolUser
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.db
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.deletePracticeByPractice
import com.example.classmanagerandroid.data.network.ClassesImplement.Companion.getClassById
import com.example.classmanagerandroid.data.remote.Chat
import com.example.classmanagerandroid.data.remote.Practice
import com.example.classmanagerandroid.data.remote.AppUser
import com.example.classmanagerandroid.data.remote.Class

class MainViewModelPractice: ViewModel() {

    var selectedPractice: Practice = Practice("","","", "","","","")
    var chat: Chat = Chat("", arrayListOf())
    var rolOfSelectedUserInCurrentPractice = RolUser("","Sin asignar")
    var currentClass: Class = Class("","","", arrayListOf(), arrayListOf(),"","")

    fun deletePractice (
        context: Context,
        navController: NavController
    ) {
        deletePracticeByPractice(
            context = context,
            navController = navController,
            selectedPractice = selectedPractice
        )
    }

    private fun getRolOfClass(
        idOfUser: String
    ) {
        currentClass.users.forEach {
            if (it.id == idOfUser) rolOfSelectedUserInCurrentPractice = it
        }
    }

    fun getPractice(
        idPractice: String,
        onFinished: () -> Unit
    ) {

        db.collection("practices")
            .document(idPractice)
            .get()
            .addOnSuccessListener {
                getClassById(
                    idOfClass = it.get("idOfClass") as String,
                    onFinished = { _, newClass ->
                        currentClass = newClass
                        getRolOfClass(CurrentUser.currentUser.id)
                    }
                )
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
                onFinished()
            }
    }

    fun updateChat(
        onFinished: () -> Unit
    ) {

        db.collection("practicesChats")
            .document(chat.id)
            .update("conversation",chat.conversation)
            .addOnCompleteListener{
                onFinished()
            }
    }

    fun getChat(
        idChat: String
    ) {
        db.collection("practicesChats")
            .document(idChat)
            .get()
            .addOnSuccessListener { document ->
                val messagesHasMap = document.get("conversation") as  MutableList<HashMap<String,Any>>
                val messages: MutableList<Message> = mutableListOf()
                messagesHasMap.forEach { message ->
                    val myUserHasMap = message.get("sentBy") as HashMap<String,Any>

                    val myUser = AppUser(
                        description = myUserHasMap.get("description") as String,
                        name = myUserHasMap.get("name") as String,
                        id = myUserHasMap.get("id") as String,
                        email = myUserHasMap.get("email") as String,
                        imgPath = myUserHasMap.get("imgPath") as String,
                        courses = myUserHasMap.get("courses") as MutableList<String>,
                        classes = myUserHasMap.get("classes") as MutableList<String>,
                        password = myUserHasMap.get("password") as String
                    )

                    messages.add(
                        Message(
                            sentOn = message.get("sentOn") as String,
                            message = message.get("message") as String,
                            sentBy = myUser
                        )
                    )
                }
                chat =
                    Chat(
                        id = document.id,
                        conversation = messages
                    )
            }
    }
}

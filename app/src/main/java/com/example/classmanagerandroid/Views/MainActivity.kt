package com.example.classmanagerandroid.Views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.Navigation.NavigationHost
import com.example.classmanagerandroid.Views.Class.MainViewModelClass
import com.example.classmanagerandroid.Views.Course.Event.MainViewModelEvent
import com.example.classmanagerandroid.Views.Course.MainViewModelCourse
import com.example.classmanagerandroid.Views.CreateClass.MainViewModelCreateClass
import com.example.classmanagerandroid.Views.CreateCourse.MainViewModelCreateCourse
import com.example.classmanagerandroid.Views.Login.ForgotPassword.MainViewModelForgotPassword
import com.example.classmanagerandroid.Views.Login.MainViewModelLogin
import com.example.classmanagerandroid.Views.Practice.MainViewModelPractice
import com.example.classmanagerandroid.Views.Register.MainViewModelRegister
import com.example.classmanagerandroid.Views.Register.PrivacyPolicies.MainViewModelPrivacyPolicies
import com.example.classmanagerandroid.data.network.AccesToDataBase.Companion.auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import me.saine.android.Views.MainAppActivity.MainViewModelMainAppView

class MainActivity : ComponentActivity() {
    private val mainViewModelLogin by viewModels<MainViewModelLogin>()
    private val mainViewModelRegister by viewModels<MainViewModelRegister>()
    private val mainViewModelMainAppView by viewModels<MainViewModelMainAppView>()
    private val mainViewModelCreateCourse by viewModels<MainViewModelCreateCourse>()
    private val mainViewModelCreateClass by viewModels<MainViewModelCreateClass>()
    private val mainViewModelCourse by viewModels<MainViewModelCourse>()
    private val mainViewModelClass by viewModels<MainViewModelClass>()
    private val mainViewModelPrivacyPolicies by viewModels<MainViewModelPrivacyPolicies>()
    private val mainViewModelForgotPassword by viewModels<MainViewModelForgotPassword>()
    private val mainViewModelPractice by viewModels<MainViewModelPractice>()
    private val mainViewModelEvent by viewModels<MainViewModelEvent>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            mainViewModelLogin.finishLogin(task)

        }
    }




    var startDestination: String = Destinations.Login.route

    @RequiresApi(Build.VERSION_CODES.O)
    @Override
    override fun onStart() {
        super.onStart()

        //auth.signOut()
        val user = auth.currentUser
        if (user != null) {
            startDestination = Destinations.MainAppView.route
            mainViewModelLogin.saveCurrentUser {
                setContent {

                    chargeScreen()

                }
            }
        } else {
            setContent {

                chargeScreen()

            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun chargeScreen() {
        NavigationHost(
            startDestination = startDestination,
            mainViewModelLogin = mainViewModelLogin,
            mainViewModelRegister = mainViewModelRegister,
            mainViewModelMainAppView = mainViewModelMainAppView,
            mainViewModelCreateCourse = mainViewModelCreateCourse,
            mainViewModelCreateClass = mainViewModelCreateClass,
            mainViewModelCourse = mainViewModelCourse,
            mainViewModelClass = mainViewModelClass,
            mainViewModelPrivacyPolicies = mainViewModelPrivacyPolicies,
            mainViewModelForgotPassword = mainViewModelForgotPassword,
            mainViewModelPractice = mainViewModelPractice,
            mainViewModelEvent = mainViewModelEvent
        )
    }
}

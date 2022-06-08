package com.example.classmanagerandroid.Screens

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.classmanagerandroid.Navigation.Destinations
import com.example.classmanagerandroid.Navigation.NavigationHost
import com.example.classmanagerandroid.Screens.Class.MainViewModelClass
import com.example.classmanagerandroid.Screens.Class.ViewMembersClass.MainViewModelViewMembersClass
import com.example.classmanagerandroid.Screens.Course.Event.MainViewModelEvent
import com.example.classmanagerandroid.Screens.Course.MainViewModelCourse
import com.example.classmanagerandroid.Screens.Course.ViewMembers.MainViewModelViewMembers
import com.example.classmanagerandroid.Screens.CreateClass.MainViewModelCreateClass
import com.example.classmanagerandroid.Screens.CreateCourse.MainViewModelCreateCourse
import com.example.classmanagerandroid.Screens.Login.ForgotPassword.MainViewModelForgotPassword
import com.example.classmanagerandroid.Screens.Login.MainViewModelLogin
import com.example.classmanagerandroid.Screens.Practice.MainViewModelPractice
import com.example.classmanagerandroid.Screens.Register.MainViewModelRegister
import com.example.classmanagerandroid.Screens.Settings.ViewModelSettings
import com.example.classmanagerandroid.data.network.AccessToDataBase.Companion.auth
import com.example.classmanagerandroid.ui.theme.ClassManagerAndroidTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.example.classmanagerandroid.Screens.MainAppActivity.MainViewModelMainAppView

class MainActivity : ComponentActivity() {
    private val mainViewModelLogin by viewModels<MainViewModelLogin>()
    private val mainViewModelRegister by viewModels<MainViewModelRegister>()
    private val mainViewModelMainAppView by viewModels<MainViewModelMainAppView>()
    private val mainViewModelCreateCourse by viewModels<MainViewModelCreateCourse>()
    private val mainViewModelCreateClass by viewModels<MainViewModelCreateClass>()
    private val mainViewModelCourse by viewModels<MainViewModelCourse>()
    private val mainViewModelClass by viewModels<MainViewModelClass>()
    private val mainViewModelForgotPassword by viewModels<MainViewModelForgotPassword>()
    private val mainViewModelPractice by viewModels<MainViewModelPractice>()
    private val mainViewModelEvent by viewModels<MainViewModelEvent>()
    private val mainViewModelViewMembers by viewModels<MainViewModelViewMembers>()
    private val mainViewModelViewMembersClass by viewModels<MainViewModelViewMembersClass>()
    private val viewModelSettings by viewModels<ViewModelSettings>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            mainViewModelLogin.finishLogin(task)

        }
    }




    var startDestination: String = Destinations.SplashScreen.route

    @RequiresApi(Build.VERSION_CODES.O)
    @Override
    override fun onStart() {
        super.onStart()
        setContent {
            chargeScreen()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun chargeScreen() {
        ClassManagerAndroidTheme{
            NavigationHost(
                startDestination = startDestination,
                mainViewModelLogin = mainViewModelLogin,
                mainViewModelRegister = mainViewModelRegister,
                mainViewModelMainAppView = mainViewModelMainAppView,
                mainViewModelCreateCourse = mainViewModelCreateCourse,
                mainViewModelCreateClass = mainViewModelCreateClass,
                mainViewModelCourse = mainViewModelCourse,
                mainViewModelClass = mainViewModelClass,
                mainViewModelForgotPassword = mainViewModelForgotPassword,
                mainViewModelPractice = mainViewModelPractice,
                mainViewModelEvent = mainViewModelEvent,
                mainViewModelViewMembers = mainViewModelViewMembers,
                mainViewModelViewMembersClass = mainViewModelViewMembersClass,
                viewModelSettings = viewModelSettings
            )
        }
    }
}

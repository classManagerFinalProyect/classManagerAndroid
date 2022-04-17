package com.example.classmanagerandroid.Navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.classmanagerandroid.Views.Class.MainClass
import com.example.classmanagerandroid.Views.Class.MainViewModelClass
import com.example.classmanagerandroid.Views.Class.ViewMembersClass.MainViewMembersClass
import com.example.classmanagerandroid.Views.Class.ViewMembersClass.MainViewModelViewMembersClass
import com.example.classmanagerandroid.Views.Course.Event.MainEvent
import com.example.classmanagerandroid.Views.Course.Event.MainViewModelEvent
import com.example.classmanagerandroid.Views.Course.MainCourse
import com.example.classmanagerandroid.Views.Course.MainViewModelCourse
import com.example.classmanagerandroid.Views.Course.ViewMembers.MainViewMembers
import com.example.classmanagerandroid.Views.Course.ViewMembers.MainViewModelViewMembers
import com.example.classmanagerandroid.Views.CreateClass.MainCreateClass
import com.example.classmanagerandroid.Views.CreateClass.MainViewModelCreateClass
import com.example.classmanagerandroid.Views.CreateCourse.MainCreateCourse
import com.example.classmanagerandroid.Views.CreateCourse.MainViewModelCreateCourse
import com.example.classmanagerandroid.Views.Login.ForgotPassword.MainForgotPassword
import com.example.classmanagerandroid.Views.Login.ForgotPassword.MainViewModelForgotPassword
import com.example.classmanagerandroid.Views.Login.MainLogin
import com.example.classmanagerandroid.Views.Login.MainViewModelLogin
import com.example.classmanagerandroid.Views.MainAppActivity.MainAppView
import com.example.classmanagerandroid.Views.Practice.MainPractice
import com.example.classmanagerandroid.Views.Practice.MainViewModelPractice
import com.example.classmanagerandroid.Views.Register.MainRegister
import com.example.classmanagerandroid.Views.Register.MainViewModelRegister
import com.example.classmanagerandroid.Views.Register.PrivacyPolicies.MainPrivacyPolicies
import com.example.classmanagerandroid.Views.Register.PrivacyPolicies.MainViewModelPrivacyPolicies
import com.example.classmanagerandroid.Views.Settings.MainSettings
import com.example.classmanagerandroid.Views.Settings.MyAccount.MainMyAccount
import com.example.classmanagerandroid.Views.Settings.MyAccountOptions.MainMyAccountOptions
import me.saine.android.Views.MainAppActivity.MainViewModelMainAppView


lateinit var navController: NavHostController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(
    startDestination: String,
    mainViewModelLogin: MainViewModelLogin,
    mainViewModelRegister: MainViewModelRegister,
    mainViewModelMainAppView: MainViewModelMainAppView,
    mainViewModelCreateCourse: MainViewModelCreateCourse,
    mainViewModelCreateClass: MainViewModelCreateClass,
    mainViewModelCourse: MainViewModelCourse,
    mainViewModelClass: MainViewModelClass,
    mainViewModelPrivacyPolicies: MainViewModelPrivacyPolicies,
    mainViewModelForgotPassword: MainViewModelForgotPassword,
    mainViewModelPractice: MainViewModelPractice,
    mainViewModelEvent: MainViewModelEvent,
    mainViewModelViewMembers: MainViewModelViewMembers,
    mainViewModelViewMembersClass: MainViewModelViewMembersClass
) {


    navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(
            route = "${Destinations.ViewMembers.route}/{idCourse}",
            arguments = listOf(navArgument("idCourse"){type = NavType.StringType})
        ) {
            val idCourse = it.arguments?.getString("idCourse")
            requireNotNull(idCourse)
            MainViewMembers(
                navController = navController,
                mainViewModelViewMembers = mainViewModelViewMembers,
                idCourse = idCourse
            )
        }
        composable(
            route = "${Destinations.ViewMembersClass.route}/{idClass}",
            arguments = listOf(navArgument("idClass"){type = NavType.StringType})
        ) {
            val idClass = it.arguments?.getString("idClass")
            requireNotNull(idClass)
            MainViewMembersClass(
                navController = navController,
                mainViewModelViewMembersClass =  mainViewModelViewMembersClass,
                idOfClass = idClass
            )
        }


        composable(route = Destinations.Login.route) {
            MainLogin(
                navController = navController,
                mainViewModelLogin = mainViewModelLogin
            )
        }
        composable(
            route = "${Destinations.Events.route}/{idCourse}",
            arguments = listOf(navArgument("idCourse"){type = NavType.StringType})
        ) {
            val idCourse = it.arguments?.getString("idCourse")
            requireNotNull(idCourse)
            MainEvent(
                navController = navController,
                mainViewModelEvent = mainViewModelEvent,
                idCourse = idCourse
            )
        }

        composable(route = Destinations.Register.route) {
            MainRegister(
                navController = navController,
                mainViewModelRegister = mainViewModelRegister
            )
        }
        composable(route = Destinations.MainAppView.route) {
            MainAppView(
                navController = navController,
                mainViewModelMainAppView = mainViewModelMainAppView
            )
        }
        composable(route = Destinations.Settings.route) {
            MainSettings(
                navController = navController,
            )
        }
        composable(route = Destinations.MyAccount.route) {
            MainMyAccount(
                navController = navController,
            )
        }
        composable(route = Destinations.MyAccountOptions.route) {
            MainMyAccountOptions(
                navController = navController,
            )
        }

        composable(route = Destinations.CreateCourse.route) {
            mainViewModelCreateCourse.createListItems()
            MainCreateCourse(
                navController = navController,
                mainViewModelCreateCourse = mainViewModelCreateCourse
            )
        }
        composable(route = Destinations.CreateClass.route) {
            MainCreateClass(
                navController = navController,
                mainViewModelCreateClass = mainViewModelCreateClass
            )
        }

        composable(route = Destinations.ForgotPassword.route) {
            MainForgotPassword(
                navController = navController,
                mainViewModelForgotPassword = mainViewModelForgotPassword
            )
        }
        composable(
            route = "${Destinations.Practice.route}/{idPractice}",
            arguments = listOf(navArgument("idPractice"){type = NavType.StringType})
        ) {
            val idPractice = it.arguments?.getString("idPractice")
            requireNotNull(idPractice)
            MainPractice(
                navController = navController,
                mainViewModelPractice = mainViewModelPractice,
                idPractice = idPractice
            )
        }

        composable(
            route = "${Destinations.Course.route}/{courseId}",
            arguments = listOf(navArgument("courseId"){type = NavType.StringType})
        ) {
            val courseId = it.arguments?.getString("courseId")
            requireNotNull(courseId)
            MainCourse(
                navController = navController,
                mainViewModelCourse = mainViewModelCourse,
                courseId = courseId
            )
        }
        composable(
            route = "${Destinations.Class.route}/{classId}",
            arguments = listOf(navArgument("classId"){type = NavType.StringType}),
            content = {
                val classId = it.arguments?.getString("classId")
                requireNotNull(classId)
                MainClass(
                    navController = navController,
                    mainViewModelClass = mainViewModelClass,
                    classId = classId
                )
            }
        )
        composable(
            route = Destinations.PrivacyPolicies.route,
            content = {
                MainPrivacyPolicies(
                    navController= navController,
                    mainViewModelPrivacyPolicies = mainViewModelPrivacyPolicies
                )
            }
        )
    }

}




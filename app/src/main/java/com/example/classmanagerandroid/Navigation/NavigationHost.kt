package com.example.classmanagerandroid.Navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.classmanagerandroid.Screens.Class.MainClass
import com.example.classmanagerandroid.Screens.Class.MainViewModelClass
import com.example.classmanagerandroid.Screens.Class.ViewMembersClass.MainViewMembersClass
import com.example.classmanagerandroid.Screens.Class.ViewMembersClass.MainViewModelViewMembersClass
import com.example.classmanagerandroid.Screens.Course.Event.MainEvent
import com.example.classmanagerandroid.Screens.Course.Event.MainViewModelEvent
import com.example.classmanagerandroid.Screens.Course.MainCourse
import com.example.classmanagerandroid.Screens.Course.MainViewModelCourse
import com.example.classmanagerandroid.Screens.Course.ViewMembers.MainViewMembers
import com.example.classmanagerandroid.Screens.Course.ViewMembers.MainViewModelViewMembers
import com.example.classmanagerandroid.Screens.CreateClass.MainCreateClass
import com.example.classmanagerandroid.Screens.CreateClass.MainViewModelCreateClass
import com.example.classmanagerandroid.Screens.CreateCourse.MainCreateCourse
import com.example.classmanagerandroid.Screens.CreateCourse.MainViewModelCreateCourse
import com.example.classmanagerandroid.Screens.Login.ForgotPassword.MainForgotPassword
import com.example.classmanagerandroid.Screens.Login.ForgotPassword.MainViewModelForgotPassword
import com.example.classmanagerandroid.Screens.Login.MainLogin
import com.example.classmanagerandroid.Screens.Login.MainViewModelLogin
import com.example.classmanagerandroid.Screens.MainAppActivity.MainAppView
import com.example.classmanagerandroid.Screens.Practice.MainPractice
import com.example.classmanagerandroid.Screens.Practice.MainViewModelPractice
import com.example.classmanagerandroid.Screens.Register.MainRegister
import com.example.classmanagerandroid.Screens.Register.MainViewModelRegister
import com.example.classmanagerandroid.Screens.Register.PrivacyPolicies.MainPrivacyPolicies
import com.example.classmanagerandroid.Screens.Settings.MainSettings
import com.example.classmanagerandroid.Screens.Settings.MyAccount.MainMyAccount
import com.example.classmanagerandroid.Screens.Settings.MyAccountOptions.MainMyAccountOptions
import com.example.classmanagerandroid.Screens.Settings.ViewModelSettings
import com.example.classmanagerandroid.Screens.MainAppActivity.MainViewModelMainAppView
import com.example.classmanagerandroid.Screens.SplashScreen.SplashScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.animation.composable

lateinit var navController: NavHostController

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
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
    mainViewModelForgotPassword: MainViewModelForgotPassword,
    mainViewModelPractice: MainViewModelPractice,
    mainViewModelEvent: MainViewModelEvent,
    mainViewModelViewMembers: MainViewModelViewMembers,
    mainViewModelViewMembersClass: MainViewModelViewMembersClass,
    viewModelSettings: ViewModelSettings
) {


    navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        builder = {
            composable(
                route = "${Destinations.ViewMembers.route}/{idCourse}",
                arguments = listOf(navArgument("idCourse"){type = NavType.StringType}),

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
                arguments = listOf(navArgument("idClass"){type = NavType.StringType}),
            ) {
                val idClass = it.arguments?.getString("idClass")
                requireNotNull(idClass)
                MainViewMembersClass(
                    navController = navController,
                    mainViewModelViewMembersClass =  mainViewModelViewMembersClass,
                    idOfClass = idClass
                )
            }


            composable(
                route = Destinations.SplashScreen.route,
            ) {
                SplashScreen(
                    navController = navController,
                    mainViewModelLogin = mainViewModelLogin
                )
            }

            composable(
                route = Destinations.Login.route,
            ) {
                MainLogin(
                    navController = navController,
                    mainViewModelLogin = mainViewModelLogin
                )
            }

            composable(
                route = "${Destinations.Events.route}/{idCourse}",
                arguments = listOf(navArgument("idCourse"){type = NavType.StringType}),
            ) {
                val idCourse = it.arguments?.getString("idCourse")
                requireNotNull(idCourse)
                MainEvent(
                    navController = navController,
                    mainViewModelEvent = mainViewModelEvent,
                    idCourse = idCourse
                )
            }

            composable(
                route = Destinations.Register.route,
                enterTransition =  {
                    slideInHorizontally(initialOffsetX = { it/2 }, animationSpec = tween(1000))

                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = { it/2 }, animationSpec = tween(1000))
                },
            ) {
                MainRegister(
                    navController = navController,
                    mainViewModelRegister = mainViewModelRegister
                )
            }
            composable(
                route = Destinations.MainAppView.route,
           ) {
                MainAppView(
                    navController = navController,
                    mainViewModelMainAppView = mainViewModelMainAppView
                )
            }
            composable(
                route = Destinations.Settings.route,
           ) {
                MainSettings(
                    navController = navController,
                    viewModelSettings = viewModelSettings
                )
            }
            composable(
                route = Destinations.MyAccount.route,
            ) {
                MainMyAccount(
                    navController = navController,
                    viewModelSettings = viewModelSettings
                )
            }
            composable(
                route = Destinations.MyAccountOptions.route,
           ) {
                MainMyAccountOptions(
                    navController = navController,
                    viewModelSettings = viewModelSettings
                )
            }

            composable(
                route = Destinations.CreateCourse.route,
           ) {
                mainViewModelCreateCourse.createListItems()
                MainCreateCourse(
                    navController = navController,
                    mainViewModelCreateCourse = mainViewModelCreateCourse
                )
            }
            composable(
                route = Destinations.CreateClass.route,
            ) {
                MainCreateClass(
                    navController = navController,
                    mainViewModelCreateClass = mainViewModelCreateClass
                )
            }

            composable(
                route = Destinations.ForgotPassword.route,
            ) {
                MainForgotPassword(
                    navController = navController,
                    mainViewModelForgotPassword = mainViewModelForgotPassword
                )
            }
            composable(
                route = "${Destinations.Practice.route}/{idPractice}",
                arguments = listOf(navArgument("idPractice"){type = NavType.StringType}),
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
                arguments = listOf(navArgument("courseId"){type = NavType.StringType}),
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
                    )
                }
            )
        }
    )
}




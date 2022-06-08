package com.example.classmanagerandroid.Navigation

sealed class Destinations (
    val route: String
) {
    object Login: Destinations(route = "Views.Login.Login")
    object Register: Destinations(route = "Views.Register.Register")
    object MainAppView: Destinations(route = "Views.MainAppActivity.MainAppActivity")
    object CreateCourse: Destinations(route = "Views.CreateCourse.CreateCourse")
    object CreateClass: Destinations(route = "Views.CreateClass.CreateClass")
    object Course: Destinations(route = "Views.Course.Course")
    object Class: Destinations(route = "Views.Class.Class")
    object PrivacyPolicies: Destinations(route = "Views.Register.PrivacyPolicies.PrivacyPolicies")
    object ForgotPassword: Destinations(route = "Views.Login.ForgotPassword.ForgotPassword")
    object Practice: Destinations(route = "Views.Practice.Practice")
    object Settings: Destinations(route = "Views.Settings.Settings")
    object MyAccount: Destinations(route = "Views.Settings.MyAccount.MyAccount")
    object MyAccountOptions: Destinations(route = "Views.Settings.MyAccountOptions.MyAccountOptions")
    object Events: Destinations(route = "Views.Course.Events.Events")
    object ViewMembers: Destinations(route = "Views.Course.ViewMembers.VieMembers")
    object ViewMembersClass: Destinations(route = "Views.Class.ViewMembersClass.ViewMembersClass")
    object SplashScreen: Destinations(route = "Views.SplashScreen")
}

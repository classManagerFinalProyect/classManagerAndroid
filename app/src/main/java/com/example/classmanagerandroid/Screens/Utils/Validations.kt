package com.example.classmanagerandroid.Screens.Utils

import java.util.regex.Pattern

fun isValidPassword(text: String) = Pattern.compile("^[a-zA-Z0-9_ñáéíóú ]{8,}\$", Pattern.CASE_INSENSITIVE).matcher(text).find()
fun isValidEmail(text: String) = Pattern.compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$", Pattern.CASE_INSENSITIVE).matcher(text).find()
fun isValidName(text: String)  = Pattern.compile("^[a-zA-Z0-9 _ºñáéíóú()]{2,20}$", Pattern.CASE_INSENSITIVE).matcher(text).find()
fun isValidDescription(text: String)  = Pattern.compile("^[a-zA-Z0-9 /_º+*?¿(),.ñáéíóú]{0,40}$", Pattern.CASE_INSENSITIVE).matcher(text).find()


//Validaciones genericas
fun isAlphabetic(text: String) = Pattern.compile("^[a-zA-Záéíóú ]+$", Pattern.CASE_INSENSITIVE).matcher(text).find()
fun isAlphanumeric(text: String) = Pattern.compile("^[a-zA-Z0-9áéíóú]{0,}$", Pattern.CASE_INSENSITIVE).matcher(text).find()




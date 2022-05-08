package com.example.classmanagerandroid.Screens.Utils

import java.util.regex.Pattern

fun isValidPassword(text: String) = Pattern.compile("^[a-zA-Z0-9_]{8,}\$", Pattern.CASE_INSENSITIVE).matcher(text).find()
fun isValidEmail(text: String) = Pattern.compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$", Pattern.CASE_INSENSITIVE).matcher(text).find()
fun isAlphabetic(text: String) = Pattern.compile("^[a-zA-Z ]+$", Pattern.CASE_INSENSITIVE).matcher(text).find()
fun isAlphanumeric(text: String) = Pattern.compile("^[a-zA-Z0-9 ]+$", Pattern.CASE_INSENSITIVE).matcher(text).find()




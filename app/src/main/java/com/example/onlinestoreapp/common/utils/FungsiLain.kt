package com.example.onlinestoreapp.common.utils

import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import java.util.regex.Pattern

object FungsiLain {

    fun inputFilter(regexInput: String) : InputFilter {
        val regex = Pattern.compile(regexInput)

        val filter = InputFilter { source, start, end, dest, deststar, destend ->
            val matcher = regex.matcher(source)
            if (matcher.find()) null else ""
        }
        return  filter
    }

    fun isValidEmail(text: String) : Boolean {
         val validateEmail = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+")
        return !TextUtils.isEmpty(text) && validateEmail.matcher(text).matches()
    }

    fun isPassValid(text: String) : String? {
        val specialCharac = Pattern.compile(".*[^a-z0-9 ]*.", Pattern.CASE_INSENSITIVE)
        val passInput = text.trim()
        var result = ""
        if (!passInput.matches(Regex(".*[0-9].*"))) {
            result = "Password should contain at least 1 digit"
        } else if(!passInput.matches(Regex(".*[a-z].*"))) {
            result = "Password should contain at least 1 lower case letter"
        }
        else if (!passInput.matches(Regex(".*[A-Z].*"))) {
            result = "Password should contain at least 1 upper case letter"
        }
        else if (!passInput.matches(Regex(".*[a-zA-Z].*"))) {
            result = "Password should contain a letter"
        }
        else if (!passInput.matches(".*[@#\$%^&+=].*".toRegex())) {
            result = "Password should contain at least 1 special character Exp.@#$%^&+="
        }
        else if (passInput.length < 6) {
            result = "Password minimal 6 characters"
        }
        else {
            result = ""
        }
        return result
    }
}
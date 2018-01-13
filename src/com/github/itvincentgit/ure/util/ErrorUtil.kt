package com.github.itvincentgit.ure.util

import com.intellij.openapi.ui.Messages

object ErrorUtil {

    fun showError(e : Throwable) {
        e.printStackTrace()
        Messages.showInfoMessage(e.message, "Error")
    }
}
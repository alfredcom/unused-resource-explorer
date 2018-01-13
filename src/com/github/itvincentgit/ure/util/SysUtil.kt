package com.github.itvincentgit.ure.util

object SysUtil {

    fun isWindow() : Boolean {
        return System.getProperty("os.name").toLowerCase().indexOf("windows") > -1
    }

    fun isMac() : Boolean {
        return System.getProperty("os.name").toLowerCase().indexOf("mac") > -1
    }
}
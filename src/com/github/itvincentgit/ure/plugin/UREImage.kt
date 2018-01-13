package com.github.itvincentgit.ure.plugin

data class UREImage(val name: String, val path: String) : UREResouces() {

    fun isImage() : Boolean {
        return path.endsWith(".png") || path.endsWith(".jpg")
    }
}
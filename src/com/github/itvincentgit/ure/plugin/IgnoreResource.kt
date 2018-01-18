package com.github.itvincentgit.ure.plugin

import com.github.itvincentgit.ure.util.FileUtil

/**
 * 忽略掉不显示的资源
 */
object IgnoreResource {

    fun filterResource(str: String) : Boolean{
        if (FileUtil.isChildOf(str, "values")) return false//过滤values目录
        return true
    }
}
package com.github.itvincentgit.ure.util

import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import java.nio.file.Paths

/**
 * 文件操作工具
 */
object FileUtil {
    fun getFileName(path: String) : String{
        return Paths.get(path).fileName.toString()
    }

    fun isChildOf(source: String, target: String): Boolean {
        return Paths.get(source).parent.endsWith(target)
    }

    /**
     * 按虚拟文件的方式删除，用此方法删除会触发idea的文件删除逻辑，例如检查svn，提示是否remove from svn
     */
    fun deleteVirtualFile(f: File, requestor: Object) {
        val virtualFile = LocalFileSystem.getInstance().findFileByIoFile(f);
        virtualFile?.delete(requestor);
    }

    fun openFile(project: Project, file: File) {
        LocalFileSystem.getInstance().findFileByIoFile(file)?.apply {
            OpenFileDescriptor(project, this).navigate(true)
        }
    }
}
package com.github.itvincentgit.ure.lint

import com.github.itvincentgit.ure.plugin.UREImage
import org.w3c.dom.Element
import java.io.File
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory

/**
 * 用DOM解释xml
 */
class LintDomParser(val file: File) {
    var unusedImages:ArrayList<UREImage> = ArrayList<UREImage>()

    fun parse() {

        val xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        xmlDoc.documentElement.normalize()
        println("Root Node: ${xmlDoc.documentElement.nodeName}")

        val issueList = xmlDoc.getElementsByTagName("issue")
        for (i in 0 until issueList.length){
            val issue = issueList.item(i) as Element
            if (issue.getAttribute("id") == "UnusedResources") {
                val locationList = issue.getElementsByTagName("location")
                for (j in 0 until  locationList.length) {
                    val location = (locationList.item(j)) as Element
                    val filePath = location.getAttribute("file")
                    println("filePath = " + filePath)
                    unusedImages.add(UREImage(getFileName(filePath), filePath))
                }
            }
        }
    }

    fun getFileName(path: String) : String{
        return Paths.get(path).fileName.toString()
    }
}
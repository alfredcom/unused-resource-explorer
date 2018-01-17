package com.github.itvincentgit.ure.lint

import com.github.itvincentgit.ure.plugin.UREImage
import com.github.itvincentgit.ure.plugin.UREResouces
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileReader
import java.nio.file.Paths

/**
 * 解析lint xml文件
 */
class LintXmlParser(val file: File) {

    val factory = XmlPullParserFactory.newInstance("org.xmlpull.mxp1.MXParserFactory", null)
    val xmlPullParser = factory.newPullParser()
    var unusedImages:ArrayList<UREImage> = ArrayList<UREImage>()

    fun parse() {

        xmlPullParser.setInput(FileReader(file))

        var eventType = xmlPullParser.getEventType()
        var inUnusedResources = false

        while(eventType != XmlPullParser.END_DOCUMENT){
            val nodeName = xmlPullParser.getName()

            when (eventType){
                XmlPullParser.START_DOCUMENT -> {}
                XmlPullParser.START_TAG -> {
                    println("START_TAG $nodeName")
                    var id = xmlPullParser.getAttributeValue(null, "id")
                    if (nodeName == "issue") {
                        println("id = " + id)
                        if (id == "UnusedResources") {
                            inUnusedResources = true
                        }
                    } else if (nodeName == "location" && inUnusedResources) {
                        var filePath = xmlPullParser.getAttributeValue(null, "file")
                        println("filePath = " + filePath)
                        unusedImages.add(UREImage(getFileName(filePath), filePath))
                    }
                }
                XmlPullParser.END_TAG -> {
                    println("END_TAG $nodeName")
                    if (nodeName == "issue") {
                        inUnusedResources = false
                    }
                }
                XmlPullParser.TEXT -> {}
            }
            eventType = xmlPullParser.next();
        }
    }

    fun getFileName(path: String) : String{
        return Paths.get(path).fileName.toString()
    }
}
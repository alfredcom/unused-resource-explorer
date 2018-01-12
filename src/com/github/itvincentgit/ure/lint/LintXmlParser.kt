package com.github.itvincentgit.ure.lint

import com.github.itvincentgit.ure.plugin.UREImage
import com.github.itvincentgit.ure.plugin.UREResouces
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileReader
import java.nio.file.Paths

class LintXmlParser(val file: File) {

    val factory = XmlPullParserFactory.newInstance()
    val xmlPullParser = factory.newPullParser()
    var unusedImages:ArrayList<UREImage>? = null

    fun parse() {

        xmlPullParser.setInput(FileReader(file))

        var eventType = xmlPullParser.getEventType()

        while(eventType != XmlPullParser.END_DOCUMENT){
            val nodeName = xmlPullParser.getName()
            when (eventType){
                XmlPullParser.START_DOCUMENT -> {}
                XmlPullParser.START_TAG -> {
                    if (nodeName == "issue") {
                        var id = xmlPullParser.getAttributeValue(null, "id")
                        println("id = " + id)
                        if (id == "UnusedResources") {
                            unusedImages = ArrayList<UREImage>()
                        }
                    } else if (nodeName == "location") {
                        var filePath = xmlPullParser.getAttributeValue(null, "file")
                        unusedImages?.add(UREImage(getFileName(filePath), filePath))
                    }
                }
                XmlPullParser.END_TAG -> {}
            }
            eventType = xmlPullParser.next();
        }
    }

    fun getFileName(path: String) : String{
        return Paths.get(path).fileName.toString()
    }
}
package com.github.itvincentgit.ure.plugin

import com.intellij.util.ui.UIUtil
import java.awt.*
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.FileInputStream
import javax.imageio.ImageIO
import javax.swing.*

/**
 * 列表每行的render
 */
class UREImageRender : JPanel(), ListCellRenderer<UREImage> {
    private val icon = JLabel()
    private val name = JLabel()
    private val path = JLabel()

    init {
        val panelText = JPanel(GridLayout(0, 1))
        panelText.add(name)
        panelText.add(path)
        add(icon, BorderLayout.WEST)
        add(panelText, BorderLayout.CENTER)
    }

    override fun getListCellRendererComponent(list: JList<out UREImage>?, value: UREImage?, index: Int,
                                              isSelected: Boolean, cellHasFocus: Boolean): Component {
        value?.let {
            if (it.isImage()) {
                try {
                    icon.icon = ImageIcon(getScaledImage(it.path, 50, 50))
                } catch (e: Exception) {
                }
            }
        }

        icon.isOpaque = true
        value.let {
            name.text = it?.name
            name.isOpaque = true
            path.text = it?.path
            path.isOpaque = true
            path.foreground = Color.blue
        }

        if (isSelected) {
            name.background = list?.selectionBackground
            icon.background = list?.selectionBackground
            path.background = list?.selectionBackground
            background = list?.selectionBackground
        } else { // when don't select
            name.background = list?.background
            icon.background = list?.background
            path.background = list?.background
            background = list?.background
        }
        return this
    }

    // 取到缩放后的图
    private fun getScaledImage(path: String, w: Int, h: Int): Image {
        val srcImg = ImageIO.read(FileInputStream(path))

        val resizedImg = UIUtil.createImage(w, h, BufferedImage.TYPE_INT_ARGB)
        val g2 = resizedImg.createGraphics()

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g2.drawImage(srcImg, 0, 0, w, h, null)
        g2.dispose()

        return resizedImg
    }
}
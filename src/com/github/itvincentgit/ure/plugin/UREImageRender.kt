package com.github.itvincentgit.ure.plugin

import com.github.itvincentgit.ure.plugin.UREImage
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.GridLayout
import javax.swing.*


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
        icon.icon = ImageIcon(value?.path)
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
}
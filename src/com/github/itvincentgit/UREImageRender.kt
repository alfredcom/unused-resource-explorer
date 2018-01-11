package com.github.itvincentgit

import com.intellij.openapi.ui.Messages
import java.awt.Component
import java.awt.BorderLayout
import java.awt.Color
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.*


class UREImageRender : JPanel(), ListCellRenderer<UREImage> {
    private val icon = JLabel()
    private val name = JLabel()
    private val path = JLabel()
//    private val delBtn = JButton()

    init {
        val panelText = JPanel(GridLayout(0, 1))
        panelText.add(name)
        panelText.add(path)
        add(icon, BorderLayout.WEST)
        add(panelText, BorderLayout.CENTER)

        setAlignmentX(Component.LEFT_ALIGNMENT)
//        delBtn.text = "delete"
//        delBtn.addActionListener({e: ActionEvent? ->
//            println("Delete click")
//        })
//        delBtn.setMnemonic(KeyEvent.VK_D)
//        add(delBtn, BorderLayout.EAST)
    }

    override fun getListCellRendererComponent(list: JList<out UREImage>?, value: UREImage?, index: Int,
                                              isSelected: Boolean, cellHasFocus: Boolean): Component {
        icon.setIcon(ImageIcon(javaClass.getResource(
                "/images/icon1.png")));
        icon.isOpaque = true
        value.let {
            name.text = it?.name
            name.isOpaque = true
            path.text = it?.path
            path.isOpaque = true
            path.setForeground(Color.blue);
        }

        if (isSelected) {
            name.setBackground(list?.getSelectionBackground())
            icon.setBackground(list?.getSelectionBackground())
            path.setBackground(list?.getSelectionBackground())
            setBackground(list?.getSelectionBackground());
        } else { // when don't select
            name.setBackground(list?.getBackground())
            icon.setBackground(list?.getBackground())
            path.setBackground(list?.getBackground())
            setBackground(list?.getBackground())
        }
        return this
    }
}
package com.github.itvincentgit;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class UREToolWindowFactory implements ToolWindowFactory {
    private JList mResouceList;
    private JPanel mToolWindowPanel;
    private ToolWindow mToolWindow;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        mToolWindow = toolWindow;

        DefaultListModel listModel = new DefaultListModel();
        listModel.addElement(new UREImage("img1", "/path/path0"));
        mResouceList.setModel(listModel);
        mResouceList.setCellRenderer(new UREImageRender());

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mToolWindowPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}

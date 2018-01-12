package com.github.itvincentgit.ure.plugin;

import com.github.itvincentgit.ure.lint.LintXmlParser;
import com.github.itvincentgit.ure.plugin.UREImage;
import com.github.itvincentgit.ure.plugin.UREImageRender;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.util.stream.Stream;

public class UREToolWindowFactory implements ToolWindowFactory {
    private JBList mResouceList;
    private JPanel mToolWindowPanel;
    private JButton mDelBtn;
    private ToolWindow mToolWindow;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        mToolWindow = toolWindow;

        DefaultListModel listModel = new DefaultListModel();
        try {
            LintXmlParser parser = new LintXmlParser(
                    new File(getClass().getResource("/test/lint-results-debug.xml").getPath()));
            parser.parse();
            parser.getUnusedImages().stream().forEach(ureImage -> listModel.addElement(ureImage));
        } catch (Exception e) {
            Messages.showInfoMessage(e.getMessage(), "Xml parse error");
        }

        listModel.addElement(new UREImage("img1", "/path/path1"));
        listModel.addElement(new UREImage("img2", "/path/path2"));
        mResouceList.setModel(listModel);
        mResouceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        mResouceList.setCellRenderer(new UREImageRender());


        mDelBtn.addActionListener(e -> Messages.showOkCancelDialog("Delete these files?", "Ask", Messages.getQuestionIcon()));

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mToolWindowPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}

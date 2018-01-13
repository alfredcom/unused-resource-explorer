package com.github.itvincentgit.ure.plugin;

import com.github.itvincentgit.ure.lint.LintXmlParser;
import com.github.itvincentgit.ure.util.ErrorUtil;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * 接入toolwindow面板
 */
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
            ErrorUtil.INSTANCE.showError(e);
        }

        //listModel.addElement(new UREImage("img1", "/path/path1"));
        //listModel.addElement(new UREImage("img2", "/path/path2"));
        mResouceList.setModel(listModel);
        mResouceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        mResouceList.setCellRenderer(new UREImageRender());


        mDelBtn.addActionListener(e -> {
                //Messages.showOkCancelDialog("Delete these files?", "Ask", Messages.getQuestionIcon())
            mResouceList.getSelectedValuesList().stream().forEach(o -> {
                try {
                    String path = ((UREImage) o).getPath();
                    System.out.println("del file: " + path);
                    File f = new File(path);
                    if (f.exists()) {
                        Application app = ApplicationManager.getApplication();
                        app.executeOnPooledThread(()-> {
                            VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(f);
                            try {
                                virtualFile.delete(UREToolWindowFactory.this);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
//                            boolean result = f.delete();
//                            System.out.println("result = " + result);
                        });
                        //Process exec = Runtime.getRuntime().exec("del " + path);


                    }
                } catch (Exception e1) {
                    ErrorUtil.INSTANCE.showError(e1);
                }
            });
        });

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mToolWindowPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}

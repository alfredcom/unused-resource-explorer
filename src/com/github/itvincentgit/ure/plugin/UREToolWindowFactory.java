package com.github.itvincentgit.ure.plugin;

import com.github.itvincentgit.ure.lint.LintXmlParser;
import com.github.itvincentgit.ure.util.ErrorUtil;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 接入toolwindow面板
 */
public class UREToolWindowFactory implements ToolWindowFactory {
    private JBList mResouceList;
    private JPanel mToolWindowPanel;
    private JButton mDelBtn;
    private JButton chooseLintReportButton;
    private ToolWindow mToolWindow;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        mToolWindow = toolWindow;

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

        chooseLintReportButton.addActionListener(e -> {
            FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor("xml");
            FileChooser.chooseFiles(descriptor, project, null, new Consumer<List<VirtualFile>>() {
                @Override
                public void consume(List<VirtualFile> virtualFiles) {
                    System.out.println("choose file " + virtualFiles);
                    DefaultListModel listModel = new DefaultListModel();
                    try {
                        LintXmlParser parser = new LintXmlParser(
                                new File(virtualFiles.get(0).getPath()));
                        parser.parse();
                        parser.getUnusedImages().stream().forEach(ureImage -> listModel.addElement(ureImage));
                    } catch (Exception e) {
                        ErrorUtil.INSTANCE.showError(e);
                    }
                    mResouceList.setModel(listModel);
                    mResouceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    mResouceList.setCellRenderer(new UREImageRender());
                }
            });
        });

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mToolWindowPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}

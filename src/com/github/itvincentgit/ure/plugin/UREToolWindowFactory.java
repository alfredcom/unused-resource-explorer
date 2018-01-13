package com.github.itvincentgit.ure.plugin;

import com.github.itvincentgit.ure.lint.LintXmlParser;
import com.github.itvincentgit.ure.util.ErrorUtil;
import com.github.itvincentgit.ure.util.SysUtil;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
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
    private JButton mOpenBtn;
    private ToolWindow mToolWindow;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        mToolWindow = toolWindow;

        mDelBtn.addActionListener(e -> {
                //Messages.showOkCancelDialog("Delete these files?", "Ask", Messages.getQuestionIcon())
            mResouceList.getSelectedValuesList().stream().forEach(o -> {
                //删除列表选择的文件
                String path = ((UREImage) o).getPath();
                System.out.println("del file: " + path);
                File f = new File(path);
                ApplicationManager.getApplication().invokeLater(() -> new WriteCommandAction(project) {
                    @Override
                    protected void run(@NotNull Result result) throws Throwable {
                        try {
                            VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(f);
                            try {
                                virtualFile.delete(UREToolWindowFactory.this);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
//                            boolean result = f.delete();
//                            System.out.println("result = " + result);

//                            Process exec = Runtime.getRuntime().exec("del " + path);
                        } catch (Exception e1) {
                            ErrorUtil.INSTANCE.showError(e1);
                        }
                    }
                }.execute());
            });
        });

        //点击按钮选择lint.xml文件
        chooseLintReportButton.addActionListener(e -> {
            FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor("xml");
            FileChooser.chooseFiles(descriptor, project, null, virtualFiles -> {
                System.out.println("choose file " + virtualFiles);
                DefaultListModel listModel = new DefaultListModel();
                //选择一个文件来解析
                try {
                    LintXmlParser parser = new LintXmlParser(
                            new File(virtualFiles.get(0).getPath()));
                    parser.parse();
                    parser.getUnusedImages().stream().forEach(ureImage -> listModel.addElement(ureImage));
                } catch (Exception e12) {
                    ErrorUtil.INSTANCE.showError(e12);
                }
                //显示在列表上
                mResouceList.setModel(listModel);
                mResouceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                mResouceList.setCellRenderer(new UREImageRender());
            });
        });

        mOpenBtn.addActionListener(e -> {
            mResouceList.getSelectedValuesList().stream().forEach(o -> {
                //打开列表选择的文件
                String path = ((UREImage) o).getPath();
                System.out.println("Open file: " + path);
                try {
                    if (SysUtil.INSTANCE.isWindow())
                        Runtime.getRuntime().exec("explorer.exe " + path);
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

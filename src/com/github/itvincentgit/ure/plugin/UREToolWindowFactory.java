package com.github.itvincentgit.ure.plugin;

import com.github.itvincentgit.ure.lint.LintDomParser;
import com.github.itvincentgit.ure.util.ErrorUtil;
import com.github.itvincentgit.ure.util.FileUtil;
import com.github.itvincentgit.ure.util.SysUtil;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.extensions.PluginId;
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
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.util.function.Predicate;

/**
 * 接入toolwindow面板
 */
public class UREToolWindowFactory implements ToolWindowFactory {
    private JBList mResouceList;
    private JPanel mToolWindowPanel;
    private JButton mDelBtn;
    private JButton chooseLintReportButton;
    private JButton mOpenBtn;
    private JButton mCleanBtn;
    private ToolWindow mToolWindow;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        mToolWindow = toolWindow;

        mDelBtn.addActionListener(e -> {
                //Messages.showOkCancelDialog("Delete these files?", "Ask", Messages.getQuestionIcon())
            mResouceList.getSelectedValuesList().stream().forEach(o -> {
                //删除列表选择的文件
                String path = ((UREImage) o).getPath();
                deleteFile(path, project);
            });
        });

        //点击按钮选择lint.xml文件
        chooseLintReportButton.addActionListener(e -> {
            FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor("xml");
            FileChooser.chooseFiles(descriptor, project, null, virtualFiles -> {
                System.out.println("choose file " + virtualFiles);
                //选择一个文件来解析
                try {
                    DefaultListModel listModel = new DefaultListModel();
                    LintDomParser parser = new LintDomParser(new File(virtualFiles.get(0).getPath()));
                    parser.parse();
                    parser.getUnusedImages().stream()
                            .filter(ureImage -> IgnoreResource.INSTANCE.filterResource(ureImage.getPath()))
                            .forEach(ureImage -> listModel.addElement(ureImage));

                    //显示在列表上
                    mResouceList.setModel(listModel);
                    mResouceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    mResouceList.setCellRenderer(new UREImageRender());
                } catch (Exception e12) {
                    ErrorUtil.INSTANCE.showError(e12);
                }
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

        mCleanBtn.addActionListener(e ->{
            if (mResouceList.getModel() == null) return;
            for (int i = 0; i < mResouceList.getModel().getSize(); i++) {
                UREImage ureImage = (UREImage) mResouceList.getModel().getElementAt(i);
                String path = ureImage.getPath();
                if (FileUtil.INSTANCE.isChildOf(path, "layout")) {
                    System.out.println("Clean layout file: " + path);
                    deleteFile(path, project);
                }
                if (FileUtil.INSTANCE.isChildOf(path, "anim")) {
                    System.out.println("Clean anim file: " + path);
                    deleteFile(path, project);
                }
                if (FileUtil.INSTANCE.isChildOf(path, "drawable")) {
                    System.out.println("Clean drawable file: " + path);
                    deleteFile(path, project);
                }
                if (ureImage.isImage()) {
                    System.out.println("Clean image file: " + path);
                    deleteFile(path, project);
                }

            }
        });

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mToolWindowPanel, "", false);
        toolWindow.getContentManager().addContent(content);

        IdeaPluginDescriptor plugin = PluginManager.getPlugin(PluginId.getId("com.github.itvincentgit.unused-resource-explorer"));
        System.out.println("Plugin version:" + plugin.getVersion());
    }

    void deleteFile(String path, Project project) {
        System.out.println("delete file: " + path);
        File f = new File(path);
        ApplicationManager.getApplication().invokeLater(() -> new WriteCommandAction(project) {
            @Override
            protected void run(@NotNull Result result) throws Throwable {
                try {
                    boolean success = f.delete();
                    System.out.println("delete file success:" + success);
                } catch (Exception e1) {
                    ErrorUtil.INSTANCE.showError(e1);
                }
            }
        }.execute());
    }
}

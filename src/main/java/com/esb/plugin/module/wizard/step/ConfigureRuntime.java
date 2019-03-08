package com.esb.plugin.module.wizard.step;

import com.esb.plugin.module.ESBModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.*;
import static java.awt.GridBagConstraints.HORIZONTAL;

public class ConfigureRuntime extends ModuleWizardStep {

    private JPanel jPanel;

    public ConfigureRuntime(WizardContext context, ESBModuleBuilder moduleBuilder) {
        jPanel = new JPanel(new GridBagLayout());

        addBrowseRuntimeHome(context, moduleBuilder);
        addHorizontalSeparator();

        buildInput("GroupId: ");
        buildInput("ArtifactId: ");
        buildInput("Version: ");

        addFiller();
    }

    @Override
    public JComponent getComponent() {
        return jPanel;
    }

    @Override
    public void updateDataModel() {

    }

    private void addBrowseRuntimeHome(WizardContext context, ESBModuleBuilder moduleBuilder) {
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        TextFieldWithBrowseButton browseButton = new TextFieldWithBrowseButton();
        browseButton.addBrowseFolderListener(new TextBrowseFolderListener(descriptor, context.getProject()) {
            @NotNull
            @Override
            protected String chosenFileToResultingText(@NotNull VirtualFile chosenFile) {
                String contentEntryPath = moduleBuilder.getContentEntryPath();
                String path = chosenFile.getPath();
                return contentEntryPath == null ? path : path.substring(StringUtil.commonPrefixLength(contentEntryPath, path));
            }
        });
        addComponentWithLabel("Runtime Home: ", browseButton);
    }

    private void addFiller() {
        JPanel filler = new JPanel();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = RELATIVE;
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = REMAINDER;
        constraints.gridheight = REMAINDER;
        constraints.fill = BOTH;
        jPanel.add(filler, constraints);
    }

    private void addHorizontalSeparator() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = RELATIVE;
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.gridwidth = REMAINDER;
        constraints.fill = HORIZONTAL;
        jPanel.add(new JSeparator(JSeparator.HORIZONTAL), constraints);
    }

    private void buildInput(String inputLabel) {
        JTextField inputTextField = new JTextField();
        addComponentWithLabel(inputLabel, inputTextField);
    }

    private void addComponentWithLabel(String labelText, JComponent component) {
        JLabel labelRuntimeHome = new JBLabel(labelText);
        labelRuntimeHome.setLabelFor(component);
        labelRuntimeHome.setVerticalAlignment(SwingConstants.TOP);

        jPanel.add(labelRuntimeHome, new GridBagConstraints(0, RELATIVE, 1, 1, 0, 0, WEST,
                VERTICAL, JBUI.insets(5, 0, 5, 0), 4, 0));
        jPanel.add(component, new GridBagConstraints(1, RELATIVE, 1, 1, 1.0, 0, CENTER,
                HORIZONTAL, JBUI.insetsBottom(5), 0, 0));
    }
}

package com.reedelk.plugin.editor.properties.commons;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.Module;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.UIUtil;
import com.reedelk.plugin.commons.DisposableUtils;
import com.reedelk.plugin.editor.properties.renderer.typedynamicvalue.DynamicValueScriptEditor;
import com.reedelk.runtime.api.commons.StringUtils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import static com.reedelk.plugin.commons.Icons.Script.Code;

public class TableDynamicCellRenderer implements TableCellRenderer, Disposable {

    private final DisposablePanel content;
    private final DynamicValueScriptEditor editor;

    public TableDynamicCellRenderer(Module module, String componentPropertyPath) {
        JLabel codeIcon = new JBLabel(Code);
        codeIcon.setOpaque(true);

        this.editor = new DynamicValueScriptEditor(module, componentPropertyPath);
        this.content = ContainerFactory.createLabelNextToComponentWithoutOuterBorder(codeIcon, editor);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.editor.setBackground(row % 2 != 0 ? UIUtil.getDecoratedRowColor() : UIUtil.getTableBackground());
        this.editor.setValue(value == null ? StringUtils.EMPTY : (String) value);
        return this.content;
    }

    @Override
    public void dispose() {
        DisposableUtils.dispose(this.editor);
    }
}

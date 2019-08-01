package com.esb.plugin.editor.properties.widget.input.script;

import com.esb.plugin.commons.Labels;
import com.esb.plugin.editor.properties.widget.DisposablePanel;
import com.esb.plugin.editor.properties.widget.input.InputChangeListener;
import com.esb.plugin.editor.properties.widget.input.script.editor.JavascriptEditor;
import com.esb.plugin.editor.properties.widget.input.script.editor.JavascriptEditorFactory;
import com.esb.plugin.editor.properties.widget.input.script.editor.JavascriptEditorMode;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.module.Module;
import com.intellij.ui.components.JBPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.esb.plugin.commons.Icons.Script;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;

public class ScriptInputField extends DisposablePanel implements DocumentListener, Disposable {

    private final Module module;
    private final ScriptContextManager context;


    private InputChangeListener<String> listener;
    private JavascriptEditor editor;
    private String value = "";

    public ScriptInputField(@NotNull Module module,
                            @NotNull ScriptContextManager context) {
        this.module = module;
        this.context = context;

        JPanel openEditorBtn = new OpenEditorButton();

        this.editor = JavascriptEditorFactory.get()
                .mode(JavascriptEditorMode.DEFAULT)
                .project(module.getProject())
                .context(context)
                .build();

        this.editor.addDocumentListener(this);

        setLayout(new BorderLayout());
        add(openEditorBtn, NORTH);
        add(editor, CENTER);
    }

    @Override
    public void documentChanged(@NotNull DocumentEvent event) {
        if (listener != null) {
            listener.onChange(event.getDocument().getText());
        }
    }

    @Override
    public void dispose() {
        this.editor.dispose();
    }

    public void addListener(InputChangeListener<String> listener) {
        this.listener = listener;
    }

    public void setValue(Object o) {
        this.value = (String) o;
        this.editor.setValue(this.value);
    }

    class OpenEditorButton extends JBPanel {

        private final Border BORDER_BTN_OPEN_EDITOR =
                BorderFactory.createEmptyBorder(3, 0, 10, 0);

        private JLabel openEditorBtn;

        OpenEditorButton() {
            openEditorBtn = new JLabel(Labels.SCRIPT_EDITOR_BTN_OPEN_EDITOR);
            openEditorBtn.setIcon(Script.Edit);
            openEditorBtn.setDisabledIcon(Script.EditDisabled);
            openEditorBtn.addMouseListener(new OpenScriptEditorDialog());

            setLayout(new BorderLayout());
            setBorder(BORDER_BTN_OPEN_EDITOR);
            add(openEditorBtn, NORTH);
        }
    }

    class OpenScriptEditorDialog extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            EditScriptDialog dialog = new EditScriptDialog(module, value, context);
            if (dialog.showAndGet()) {
                value = dialog.getValue();
                listener.onChange(value);
                editor.setValue(value);
            }
        }
    }
}

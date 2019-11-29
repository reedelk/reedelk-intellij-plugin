package com.reedelk.plugin.editor.properties.renderer.typescript.scriptactions;

import com.intellij.openapi.module.Module;
import com.reedelk.plugin.editor.properties.commons.ClickableLabel;
import com.reedelk.plugin.editor.properties.commons.DialogConfirmAction;
import com.reedelk.plugin.service.module.ScriptService;
import com.reedelk.plugin.service.module.impl.ScriptResource;

import java.awt.event.MouseEvent;

import static com.reedelk.plugin.commons.Icons.Config.Delete;
import static com.reedelk.plugin.commons.Icons.Config.DeleteDisabled;
import static com.reedelk.plugin.commons.Messages.Script;

class ActionDeleteScript extends ClickableLabel {

    private final Module module;
    private ScriptResource selected;

    ActionDeleteScript(Module module) {
        super("Delete", Delete, DeleteDisabled);
        this.module = module;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        DialogConfirmAction dialogConfirmDelete = new DialogConfirmAction(
                module,
                Script.DIALOG_DELETE_TITLE.format(),
                Script.DIALOG_DELETE_CONFIRM_MESSAGE.format());

        if (dialogConfirmDelete.showAndGet()) {
            ScriptService.getInstance(module).removeScript(selected.getPath());
        }
    }

    public void onSelect(ScriptResource value) {
        setEnabled(value.isRemovable());
        this.selected = value;
    }
}

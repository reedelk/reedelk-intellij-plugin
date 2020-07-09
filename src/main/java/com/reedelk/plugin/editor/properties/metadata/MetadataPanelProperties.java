package com.reedelk.plugin.editor.properties.metadata;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.reedelk.plugin.commons.Colors;
import com.reedelk.plugin.editor.properties.commons.DisposablePanel;
import com.reedelk.plugin.editor.properties.commons.FormBuilder;
import com.reedelk.plugin.service.module.impl.component.metadata.MetadataTypeDTO;
import com.reedelk.plugin.service.module.impl.component.metadata.MetadataTypeItemDTO;

import javax.swing.*;
import java.awt.*;

import static com.intellij.util.ui.JBUI.Borders.emptyLeft;
import static com.reedelk.plugin.editor.properties.metadata.RendererDefault.LEFT_OFFSET;
import static com.reedelk.plugin.editor.properties.metadata.RendererUtils.propertyEntry;
import static com.reedelk.runtime.api.commons.StringUtils.isNotBlank;
import static java.util.Comparator.comparing;
import static javax.swing.Box.createHorizontalGlue;

public class MetadataPanelProperties extends DisposablePanel {

    public MetadataPanelProperties(MetadataTypeDTO metadataType) {
        super(new GridBagLayout());
        setBackground(JBColor.WHITE);
        metadataType.getProperties().stream()
                .sorted(comparing(typeDTO -> typeDTO.name)) // sort properties by name
                .forEach(typeItemDTO -> renderTypeItem(this, typeItemDTO));
    }

    private void renderTypeItem(DisposablePanel content, MetadataTypeItemDTO typeItemDTO) {
        if (isNotBlank(typeItemDTO.displayType)) {
            String label = propertyEntry(typeItemDTO.name, typeItemDTO.displayType);
            createLabelAndAdd(content, label);

        } else {
            MetadataTypeDTO complex = typeItemDTO.complex;
            String text = typeItemDTO.name + " : " + complex.getDisplayType();
            if (complex.getProperties().isEmpty()) {
                createLabelAndAdd(content, text);
            } else {
                JComponent payload = RendererDefault.createCollapsiblePanel(text, complex);
                payload.setBorder(emptyLeft(LEFT_OFFSET));
                FormBuilder.get().addFullWidthAndHeight(payload, content);
            }
        }
    }

    private void createLabelAndAdd(DisposablePanel content, String label) {
        JBLabel attributes = new JBLabel(label, JLabel.LEFT);
        attributes.setForeground(Colors.PROPERTIES_PROPERTY_TITLE);
        attributes.setBorder(emptyLeft(LEFT_OFFSET));
        FormBuilder.get().addLabel(attributes, content);
        FormBuilder.get().addLastField(createHorizontalGlue(), content);
    }
}

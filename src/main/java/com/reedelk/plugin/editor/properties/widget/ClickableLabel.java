package com.reedelk.plugin.editor.properties.widget;

import com.reedelk.runtime.api.commons.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ClickableLabel extends JLabel implements MouseListener {

    enum IconAlignment {
        LEFT,
        RIGHT
    }

    private final OnClickAction action;

    public ClickableLabel(String text, Icon icon, Icon disabledIcon) {
        this(text, icon, disabledIcon, null);
    }

    public ClickableLabel(Icon icon, Icon disabledIcon, OnClickAction action) {
        this(StringUtils.EMPTY, icon, disabledIcon, IconAlignment.LEFT, action);
    }

    public ClickableLabel(String text, Icon icon, Icon disabledIcon, OnClickAction action) {
        this(text, icon, disabledIcon, IconAlignment.LEFT, action);
    }

    public ClickableLabel(String text, Icon icon, Icon disabledIcon, IconAlignment iconAlignment, OnClickAction action) {
        setText(text);
        setIcon(icon);
        setDisabledIcon(disabledIcon);
        addMouseListener(this);
        this.action = action;

        if (iconAlignment.equals(IconAlignment.LEFT)) {
            setHorizontalTextPosition(SwingConstants.RIGHT); // Icon pushed to the left
        } else {
            setHorizontalTextPosition(SwingConstants.LEFT); // Icon pushed to the right
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.action.onClick();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    public interface OnClickAction {
        void onClick();
    }
}

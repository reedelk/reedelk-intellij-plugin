package com.esb.plugin.component.scanner;

import com.esb.plugin.commons.Icons;
import com.esb.plugin.commons.Images;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import javax.swing.*;
import java.awt.*;

public class ComponentAnalyzerContext {

    private final ScanResult scanResult;

    public ComponentAnalyzerContext(ScanResult scanResult, ComponentIconAndImageLoader componentIconAndImageLoader) {
        this.scanResult = scanResult;
    }

    ClassInfo getClassInfo(String fullyQualifiedClassName) {
        return scanResult.getClassInfo(fullyQualifiedClassName);
    }

    Image getImageByClassName(String fullyQualifiedClassName) {
        return Images.Component.get(fullyQualifiedClassName);
    }

    Icon getIconByClassName(String fullyQualifiedClassName) {
        return Icons.Component.get(fullyQualifiedClassName);
    }
}

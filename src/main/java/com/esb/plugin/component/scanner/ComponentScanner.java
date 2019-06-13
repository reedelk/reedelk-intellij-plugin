package com.esb.plugin.component.scanner;

import com.esb.api.annotation.ESBComponent;
import com.esb.plugin.commons.PackageToPath;
import com.esb.plugin.component.domain.ComponentDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.esb.plugin.component.domain.ComponentClass.UNKNOWN;
import static java.lang.String.format;


public class ComponentScanner {

    private final Logger LOG = Logger.getInstance(ComponentScanner.class);

    public List<ComponentDescriptor> from(Package targetPackage) {
        ScanResult scanResult = instantiateScanner()
                .whitelistPaths(PackageToPath.convert(targetPackage.getName()))
                .scan();
        // Unknown components are filtered out
        return filterOutUnknownClassComponents(processScanResult(scanResult));
    }

    public List<ComponentDescriptor> from(String targetPath) {
        ScanResult scanResult = instantiateScanner()
                .overrideClasspath(targetPath)
                .scan();
        // Unknown components are filtered out
        return filterOutUnknownClassComponents(processScanResult(scanResult));
    }

    private List<ComponentDescriptor> processScanResult(ScanResult scanResult) {
        List<ComponentDescriptor> componentDescriptors = new ArrayList<>();
        ClassInfoList classInfoList = scanResult.getClassesWithAnnotation(ESBComponent.class.getName());
        for (ClassInfo classInfo : classInfoList) {
            try {
                ComponentAnalyzer componentAnalyzer = ComponentAnalyzerFactory.get(scanResult);
                ComponentDescriptor descriptor = componentAnalyzer.analyze(classInfo);
                componentDescriptors.add(descriptor);
            } catch (Exception e) {
                LOG.error(format("Error, while processing component " +
                        "definition with qualified name '%s'", classInfo.getName()), e);
            }
        }
        return componentDescriptors;
    }

    private ClassGraph instantiateScanner() {
        return new ClassGraph()
                .enableFieldInfo()
                .enableAnnotationInfo()
                .ignoreFieldVisibility();
    }

    private static List<ComponentDescriptor> filterOutUnknownClassComponents(List<ComponentDescriptor> componentDescriptorList) {
        return componentDescriptorList.stream()
                .filter(descriptor -> !UNKNOWN.equals(descriptor.getComponentClass()))
                .collect(Collectors.toList());
    }
}

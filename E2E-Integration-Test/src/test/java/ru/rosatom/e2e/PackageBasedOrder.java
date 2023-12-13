package ru.rosatom.e2e;

import org.junit.jupiter.api.ClassDescriptor;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.ClassOrdererContext;

import java.util.Comparator;
import java.util.List;

public class PackageBasedOrder implements ClassOrderer {

    private Comparator<ClassDescriptor> packageNameBasedComparator = Comparator.comparing(this::getPackageLastFolderName);

    @Override
    public void orderClasses(ClassOrdererContext classOrdererContext) {
        List<? extends ClassDescriptor> testClasses = classOrdererContext.getClassDescriptors();
        testClasses.sort(packageNameBasedComparator);
    }


    private String getPackageLastFolderName(ClassDescriptor clazz) {
        String[] packageName = clazz.getTestClass().getPackageName().split("\\.");
        return packageName[packageName.length - 1];
    }
}

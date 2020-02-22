package learning.Other.TESTNG

import org.testng.IAnnotationTransformer
import org.testng.annotations.ITestAnnotation

import java.lang.reflect.Constructor
import java.lang.reflect.Method

class AnnotationTransformer implements IAnnotationTransformer {
    @Override
    void transform(ITestAnnotation iTestAnnotation, Class aClass, Constructor constructor, Method method) {
        iTestAnnotation.setRetryAnalyzer(RetryAnalyzer.class)
    }
}

package learning.Other.annotations;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactoryFinder;
import org.openqa.selenium.support.FindBy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@PageFactoryFinder(FindBy.FindByBuilder.class)
public @interface FindByAutomationID {
    String prefix() default "";
    String value();
    String extension() default "";
}



/*
Annotation Applied to Java Code
-------------------------------
@Override : Checks that the function is an override, Causes a compile warning if the function is not found in one of the parent class

@Deprecated: Marks the function as obsolete, caused the compile warning if the function is used

@SupressWarning :  If you dont want to get any warning from compiler for the known things, then you can use this annotation
You are calling a deprecated method, to avoid compiler warning

Annotations applied to other annotations
----------------------------------------
Members can be declared as shown above, it looks like methods. We should not provide implementations for these members.
All annotations extend java.land.annotation.Annotation interface. Annotations cannot include any extends cause

@Retention:
Retention Policy: CLASS, SOURCE, RUNTIME - A retention policy determines at what point annotation should be discarded
SOURCE - will be retained only with source code, and discarded during compile time
CLASS - will be retained till compiling the code, and discarded during runtime
RUNTIME - Will be available to JVM through runtime
Default retention policy is CLASS

@Target : Marks another annotation to restrict what kind of java elements the annotation may be applied to
@Target(value={TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE}), we can have multiple values in the target annotation

@Documented: Marks another annotation for inclusion in the documentation

@Inherited - Marks another annotation to be inherited to subclasses of annotated class [by default annotations are not inherited to subclasses]



 */
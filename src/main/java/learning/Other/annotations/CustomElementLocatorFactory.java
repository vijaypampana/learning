package learning.Other.annotations;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;

public class CustomElementLocatorFactory implements ElementLocatorFactory {
    private final WebDriver oDriver;

    public CustomElementLocatorFactory(WebDriver oDriver) {
        this.oDriver = oDriver;
    }

    @Override
    public ElementLocator createLocator(Field field) {
        if(field.isAnnotationPresent(FindByAutomationID.class)) {
            return new CustomElementLocator(oDriver, field);
        } else {
            return new DefaultElementLocator(oDriver, field);
        }
    }
}

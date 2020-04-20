package learning.Other.annotations;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

public class AutomationPageFactory {

    public static void initElements(final WebDriver oDriver, final Object page) {
        initElements(new CustomElementLocatorFactory(oDriver), page);
    }

    public static void initElements(final ElementLocatorFactory factory, final Object page) {
        PageFactory.initElements(new CustomFieldDecorator((CustomElementLocatorFactory) factory), page);
    }
}

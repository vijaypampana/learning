package learning.Other.annotations;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;
import java.util.List;

public class CustomElementLocator implements ElementLocator {

    private static final String AUTO_ID_FORMAT = "%s[data-test-id='%s']%s";
    private final SearchContext searchContext;
    private final FindByAutomationID autoId;

    public CustomElementLocator(SearchContext searchContext, Field field) {
        this.searchContext = searchContext;
        this.autoId = field.getAnnotation(FindByAutomationID.class);
    }

    @Override
    public WebElement findElement() {
        return searchContext.findElement(By.cssSelector(String.format(AUTO_ID_FORMAT, autoId.prefix(), autoId.value(), autoId.extension())));
    }

    @Override
    public List<WebElement> findElements() {
        return searchContext.findElements(By.cssSelector(String.format(AUTO_ID_FORMAT, autoId.prefix(), autoId.value(), autoId.extension())));
    }
}

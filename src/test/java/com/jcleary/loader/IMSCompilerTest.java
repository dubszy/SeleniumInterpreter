package com.jcleary.loader;

import com.jcleary.ui.Window;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by julian on 6/23/2016.
 */
public class IMSCompilerTest {

    //@Test // TODO This doesn't work yet, gradle is not pulling in the whole classpath or something, can't find java.lang
    public void critPathStdLib() throws InterruptedException {
        final Window window = new Window(null);
        ((Runnable) () -> window.init()).run();

        Thread.sleep(1000);
        window.setInput("System.out.println(\"Testing...123\");");
        Thread.sleep(2000);
        window.runSnippet(); // TODO fails here
        Thread.sleep(6000);

        assert window.getOutput().equals("Test...123");
    }

    //@Test // TODO This doesn't work yet, gradle is not pulling in the whole classpath or something, can't find java.lang
    public void critPathWebDriver() throws InterruptedException {

        final WebDriver driver = new FirefoxDriver();
        try {
            final Window window = new Window(driver);
            ((Runnable) () -> window.init()).run();

            Thread.sleep(1000);
            window.setInput("driver.get(\"http://www.google.com\");");
            Thread.sleep(2000);
            window.runSnippet(); // TODO fails here
            Thread.sleep(6000);

            assert driver.getCurrentUrl().equals("https://www.google.com/?gws_rd=ssl");
        } finally {
            driver.close();
        }
    }
}

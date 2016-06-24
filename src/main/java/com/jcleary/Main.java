package com.jcleary;

import com.jcleary.ui.Window;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by julian on 6/23/2016.
 */
public class Main {
    public static void main(String[] args) throws Exception {

        WebDriver driver = new FirefoxDriver();
        Window window = new Window(driver);

        window.init();
    }
}

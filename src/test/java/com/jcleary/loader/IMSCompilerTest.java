package com.jcleary.loader;

import com.jcleary.ui.Window;

/**
 * Created by julian on 6/23/2016.
 */
public class IMSCompilerTest {

    //@Test
    public void critPath() throws InterruptedException {
        final Window window = new Window(null);
        ((Runnable) () -> window.init()).run();

        Thread.sleep(1000);
        window.setInput("System.out.println(\"Testing...123\");");
        Thread.sleep(2000);
        window.runSnippet();
        System.out.println("Ran...");
        Thread.sleep(6000);

        assert window.getOutput().equals("Test...123");
    }
}

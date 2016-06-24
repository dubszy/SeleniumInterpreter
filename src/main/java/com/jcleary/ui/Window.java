package com.jcleary.ui;

import com.jcleary.loader.IMSCompiler;
import org.openqa.selenium.WebDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * The gui window.  This gui consists of an input text area on top, an stdout text area below.  And a run button that
 * triggers the java code in the top to be injected into a template, compiled and ran.
 *
 * Code inputted should be treated as if it was in a static method.
 *
 * Created by julian on 6/23/2016.
 */
public class Window {

    private final WebDriver driver;

    private JFrame mainFrame;
    private JTextArea input;
    private JTextArea output;
    private JButton run;

    public Window(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Init all the gui components and attach action listeners
     */
    public void init() {

        /*/***********************
         * Initialize components *
         *************************/
        mainFrame = new JFrame("Selenium interpreter");
        mainFrame.setSize(600, 400);
        mainFrame.setLayout(new GridLayout(3, 1));

        input = new JTextArea();

        output = new JTextArea();
        output.setEditable(false);
        output.setBackground(Color.LIGHT_GRAY);

        run = new JButton("Run");

       /*/****************
         * Add Listeners *
         *****************/
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                driver.quit();
                System.exit(0);
            }
        });

        run.addActionListener(e -> {
            PrintStream ps = System.out;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));

            String name = "SeleniumRunner";

            String src =
                    "import org.openqa.selenium.*;" +
                    "public class " + name + " {" +
                    "    public static void run(WebDriver driver) {" +
                    //"        try {" +
                                 getInput() +
                    //"        } catch (Throwable e) {" +
                    //"            System.out.println(e);" +
                    //"        }" +
                    "    }" +
                    "}";

                    IMSCompiler imsCompiler = null;
            try {
                imsCompiler = new IMSCompiler(name, src);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            try {
                imsCompiler.execute(driver);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            byte[] b = baos.toByteArray();

            String s = new String(b);

            setOutput(s);
        });

       /*/******************
         * Link components *
         *******************/
        mainFrame.add(input);
        mainFrame.add(output);
        mainFrame.add(run);
        mainFrame.setVisible(true);

    }

    /**
     * Get the text currently in the output component.  This is used for testing purposes.
     *
     * @return
     */
    public String getOutput() {
        return output.getText();
    }

    /**
     * Set the text in the output component.  This is where text should be put from stdout.
     *
     * @param text
     */
    public void setOutput(String text) {
        output.setText(text);
    }

    /**
     * Get the text currently in the input component.
     *
     * @return
     */
    public String getInput() {
        return input.getText();
    }

    /**
     * Set the text in the input component.  This is used for testing purposes.
     *
     * @param text
     */
    public void setInput(String text) {
        input.setText(text);
    }

    /**
     * Simulate a click on the run button.
     */
    public void runSnippet() {
        run.doClick();
    }
}

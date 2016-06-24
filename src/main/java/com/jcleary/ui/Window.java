package com.jcleary.ui;

import com.jcleary.loader.IMSCompiler;
import org.openqa.selenium.WebDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
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

    public void init() {

        // Initialize components
        mainFrame = new JFrame("Selenium interpreter");
        mainFrame.setSize(600, 400);
        mainFrame.setLayout(new GridLayout(3, 1));

        input = new JTextArea();
        input.setSize(600, 190);
        output = new JTextArea();
        input.setSize(600, 190);


        run = new JButton("Run");
        //run.setSize(100, 20);

        // Add listeners
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                driver.quit();
                System.exit(0);
            }
        });

        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrintStream ps = System.out;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                System.setOut(new PrintStream(baos));

                String name = "SeleniumRunner";

                String src =
                        "import org.openqa.selenium.WebDriver;" +
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
            }
        });

        //Link components
        mainFrame.add(input);
        mainFrame.add(output);
        mainFrame.add(run);
        mainFrame.setVisible(true);

    }

    public String getOutput() {
        return output.getText();
    }

    public void setOutput(String text) {
        output.setText(text);
    }

    public String getInput() {
        return input.getText();
    }

    public void setInput(String text) {
        input.setText(text);
    }

    public void runSnippet() {
        run.doClick();
    }
}

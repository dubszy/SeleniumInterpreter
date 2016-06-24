package com.jcleary.loader;

import org.openqa.selenium.WebDriver;
import sun.tools.java.*;
import sun.tools.javac.BatchEnvironment;
import sun.tools.javac.SourceClass;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
/**
 * Created by julian on 6/23/2016.
 */
public class IMSCompiler  {
    protected String name;
    protected String source;
    protected Class compiledClass;

    public IMSCompiler(String name, String source) throws Exception {
        this.name = name;
        this.source = source;
        loadClass();
    }

    protected void loadClass() throws IOException, ClassNotFound, InterruptedException {
        ClassPath cp = new ClassPath(System.getProperty("java.class.path"));
        OutputStream os = System.out;
        BatchEnvironment be = new BatchEnvironment(os, cp);
        be.flags = 0x41004;
        be.majorVersion = 45;
        be.minorVersion = 3;
        be.covFile = null;
        be.setCharacterEncoding(null);

        be.parseFile(new InMemorySourceClassFile(name + ".java", source));

        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);

        be.flushErrors();
        Vector v = new Vector();

        for (Enumeration eenum = be.getClasses(); eenum.hasMoreElements();) {
            v.addElement(eenum.nextElement());
        }

        for (int i=0; i<v.size(); i++) {
            ClassDeclaration cd = (ClassDeclaration) v.elementAt(i);
            Object object = cd.getClassDefinition(be);

            if (object instanceof SourceClass) {
                SourceClass sourceclass = (SourceClass) object;
                cd.setDefinition(sourceclass, 5);
                SourceClass sourceclass1 = (SourceClass) object;
                baos.reset();
                sourceclass.compile(baos);
            }
            else if (object instanceof BinaryClass) {
                BinaryClass binaryclass = (BinaryClass) object;
                binaryclass.write(be, baos);
            }
            byte[] b = baos.toByteArray();

            InMemorySourceCompilerClassLoader myClassLoader = new InMemorySourceCompilerClassLoader();
            compiledClass = myClassLoader.getClassFromBytes(name, b);
        }
    }

    public void execute(WebDriver driver) throws Exception {

        Method m = compiledClass.getMethod("run", new Class[]{ WebDriver.class });
        m.invoke(null, driver);
    }

    static class InMemorySourceCompilerClassLoader extends ClassLoader
    {
        public Class getClassFromBytes(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }
}

class InMemorySourceClassFile extends ClassFile
{
    private String filename;
    private String text;

    public InMemorySourceClassFile(String filename, String text) {
        super(new File(filename));
        this.filename = filename;
        this.text = text;
    }

    public String getAbsoluteName() {
        return filename;
    }

    public boolean exists() {
        return true;
    }

    public InputStream getInputStream() {
        return new StringBufferInputStream(text);
    }

    public String getName() {
        return filename;
    }

    public String getPath() {
        return "";
    }

    public boolean isDirectory() {
        return false;
    }

    public boolean isZipped() {
        return false;
    }

    public long lastModified() {
        return new Date().getTime();
    }

    public long length() {
        return text.length();
    }

    public String toString() {
        return filename;
    }
}

package com.github.tamnguyenbbt;

import com.github.tamnguyenbbt.dom.*;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;

public class CodeGeneratorTest
{
    private DomUtil domUtil;

    @Before
    public void init()
    {
        domUtil = new DomUtil();
    }

    @Test

    public void simpleTestMethodCodeGen() throws IOException
    {
        //Arrange
        String resourcePath = getClass().getClassLoader().getResource("google-signup.html").getFile();
        Document document = domUtil.htmlFileToDocument(resourcePath);
        CodeGenerator codeGenerator = new CodeGenerator(document, new SeleniumCodeGenAssociation());

        //Act
        String className = codeGenerator.getCodeGenClassName();
        System.out.println(className);
        String folder = CodeGeneratorTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println(folder);
        String fileName = folder + File.separator + className + "Generated.java";
        System.out.println(fileName);
        codeGenerator.generatePageObjectModelClass();
    }
}
